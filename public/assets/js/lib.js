const getInputApiUrl = () => $("#api-url-input").val();

const disableApiUrlBtn = (isDisabled = true) => $('#api-url-form button[type="submit"]').prop("disabled", isDisabled);

const fetchLocales = (lang = "es") => fetch(`./assets/locale/${lang}.json`).then((result) => result.json());

const fetchCategories = () => fetch(`${apiUrl}/api/categories`).then((result) => result.json());

const createCategory = (name) => 
  fetch(`${apiUrl}/api/categories`, { 
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name }),
  })
    .then((result) => result.json());

const editCategory = (id, name) =>
  fetch(`${apiUrl}/api/categories/${id}`, {
    method: 'PATCH',
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ name }),
  })
    .then((result) => result.json());

const removeCategory = (id) =>
  fetch(`${apiUrl}/api/categories/${id}`, {
    method: 'DELETE',
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((result) => result.json());


const fetchProducts = () =>
  fetch(`${apiUrl}/api/products`)
    .then((result) => result.json());

const findProductByName = (name) =>
  fetch(`${apiUrl}/api/products/query?name=${encodeURIComponent(name)}`)
    .then((result) => result.json());

const addProduct = (product) =>
  fetch(`${apiUrl}/api/products`, {
    method: 'POST',
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ ...product, quantity: parseInt(product.quantity), price: parseFloat(product.price) })
  })
    .then((result) => result.json());

const editProduct = (id, product) =>
  fetch(`${apiUrl}/api/products/${id}`, {
    method: 'PATCH',
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ ...product, quantity: parseInt(product.quantity), price: parseFloat(product.price) })
  })
    .then((result) => result.json());

const buildError = (message) =>
  $('<div class="p-3 text-bg-danger rounded-3"></div>')
    .text(message);

const buildCategoryItem = (id, name, includeEdition) => {
  const element = $(`<li class="h-50px d-flex justify-content-between list-group-item" id="category-${id}"></li>`)
    .append(
      $(`<div class="d-flex align-items-center"></div>`)
        .append($(`<input class="form-check-input me-1" name="category" id="category-${id}" type="radio" value="" ${includeEdition ? "disabled" : ""}></input>`))
        .append(
          $(`<label class="form-check-label" for="category-${id}"></label>`)
            .text(name)
        )
    );

  if (includeEdition) {
    element.append(
      $(`
        <div class="d-flex justify-content-end gap-1">
          <button class="btn btn-primary" type="button" title="Editar" data-category="${id}" data-bs-toggle="modal" data-bs-target="#edit-category-modal">
            <i class="pe-none bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-danger" type="button" title="Eliminar" data-category="${id}" data-bs-toggle="modal" data-bs-target="#remove-category-modal">
            <i class="pe-none bi bi-x-circle"></i>
          </button>
        </div>
      `)
    );
  }

  return element;
}

const buildProductItem = ({ id, name, description, price, quantity, imageUrl, categories }) => 
  $(`
    <li id="product-${id}" class="card" style="width: 12rem;" data-product="${id}">
      <img src="${imageUrl}" height="180px"" class="card-img-top object-fit-cover border rounded" alt="${description}">
      <div class="card-header">
        <h5 class="card-title">${name}</h5>
        <div>
          <h6 class="card-text">${description}</h6>
          ${categories.length ? categories.map((data) => `<span class="badge bg-primary" data-category="${data.id}">${data.name}</span>`) : `<span class="fst-italic">Sin categorias</span>`}
        </div>
      </div>
      <div class="card-body">
        <p class="fs-5 card-text fw-bold">AR$ ${price}</p>
        <p class="fs-6 card-text">Cant. disponible: ${quantity}</p>
      </div>
    </li>
  `);

const buildSearchProductItem = ({ id, name, description, imageUrl }) => 
  $(`
    <li class="card list-unstyled" id="search-product-${id}" data-product="${id}" style="width: 12rem">
      <button type="button" class="card-body text-center unstyled" data-product="${id}" data-bs-toggle="modal" data-bs-target="#edit-product-modal">
        <img src="${imageUrl}" class="pe-none card-img-top" style="width: 5rem" alt="${description}">
        <div class="pe-none card-title">${name}</div>
      </button>
    </li>
  `);

let localeStore;
let categoriesById;
let productsById;
let apiUrl = window.sessionStorage.getItem("api_url") ?? "http://localhost:8000";

const localizeToken = (token) => localeStore?.get(token) ?? token;

async function init() {
  const localeStrings = await fetchLocales();
  localeStore = new Map(Object.entries(localeStrings));

  $("#api-url-form").on("submit", (event) => {
    event.preventDefault();
    apiUrl = getInputApiUrl();
    disableApiUrlBtn(true);
    window.sessionStorage.setItem("api_url", apiUrl);
  });
  $("#api-url-input").val(apiUrl);
  $("#api-url-input").on("keyup", () => {
    const inputData = getInputApiUrl();
    disableApiUrlBtn(inputData === apiUrl);
  });
}

const renderCategories = (includeEdition = false) => {
  if (categoriesById) {
    $("#category-list")
        .empty()
        .append(...Array.from(categoriesById).map(([ id, name ]) => buildCategoryItem(id, name, includeEdition)));

    $("#category-list").on("click", "button", (event) => {
      event.stopImmediatePropagation();

      const button = $(event.target);
      const action = button.attr("data-action");
    })
  }
};

const toggleAddCategoryButton = (show) => {
  if ($("#add-category-btn").length) {
    $("#add-category-btn").remove();
  }

  if (show) {
    $(`
      <button id="add-category-btn" class="btn btn-success" type="button" title="Añadir categoría" data-bs-toggle="modal" data-bs-target="#add-category-modal">
        <i class="pe-none bi bi-plus-circle"></i>
      </button>
    `)
      .insertBefore($("#edit-categories-btn"));
  }
};

const loadCategories = async (includeEdition = false) => {
  try {
    const result = await fetchCategories();

    if (result.success) {
      categoriesById = new Map(result.data.map(({ id, name }) => ([id, name])));
      renderCategories(includeEdition);
      await configureProducts();
    } else {
      $("#category-list")
        .empty()
        .append(buildError(localizeToken(result.data.token)));
    }
  } catch (error) {
    console.error(error);
  }
};

async function configureCategories() {
  await loadCategories();
  
  $("#edit-categories-btn").on("click", () => {
    const isEditing = $("#edit-categories-btn").hasClass("btn-danger");
    if (isEditing) {
      $("#edit-categories-btn")
        .removeClass("btn-danger")
        .addClass("btn-success")
        .prop("title", "Editar categorías");
      $("#edit-categories-btn-pencil-icon").prop("hidden", false);
      $("#edit-categories-btn-cancel-icon").prop("hidden", true);

      $("#findby-category-btn").prop("disabled", false);
      toggleAddCategoryButton(false);
    } else {
      $("#edit-categories-btn")
        .removeClass("btn-success")
        .addClass("btn-danger")
        .prop("title", "Cancelar edición");
      $("#edit-categories-btn-pencil-icon").prop("hidden", true);
      $("#edit-categories-btn-cancel-icon").prop("hidden", false);

      $("#findby-category-btn").prop("disabled", true);
      toggleAddCategoryButton(true);
    }

    renderCategories(!isEditing);
  });

  $("#add-category-form").on("submit", (event) => {
    event.preventDefault();

    const toggleModalButtons = (disabled) => {
      $("#add-category-modal-add-btn").prop("disabled", disabled);
      $("#add-category-modal-close-btn").prop("disabled", disabled);
    };

    const categoryName = $("#add-category-modal-category-name").val();

    $("#add-category-modal-category-name").val("")
    $("#add-category-error").prop("hidden", true)
    toggleModalButtons(true);
    createCategory(categoryName)
      .then((result) => {
        toggleModalButtons(false);

        if (result.success) {
          bootstrap.Modal.getInstance($("#add-category-modal").get(0)).hide();
          loadCategories(true);
        } else {
          $("#add-category-error").prop("hidden", false).text(localizeToken(result.data.token));
        }
      })
  });

  $(document).on("hide.bs.modal", () => {
    $(".modal-backdrop").remove();
  });

  $("#edit-category-modal").on("show.bs.modal", (event) => {
    const button = $(event.relatedTarget);
    const categoryId = parseInt(button.attr("data-category"));
    const categoryName = categoriesById.get(categoryId);

    if (categoryName) {
      $("#edit-category-modal-category-name").val(categoryName);
      $("#edit-category-modal-category-name").on("keyup", () => {
        $("#edit-category-modal-save-btn").prop("disabled", $("#edit-category-modal-category-name").val().toLowerCase() === categoryName.toLowerCase())
      });
      $("#edit-category-form").on("submit", (event) => {
        event.preventDefault();
        event.stopImmediatePropagation();

        const disableButtons = (disabled) =>  {
          $("#edit-category-modal-save-btn").prop("disabled", disabled);
          $("#edit-category-modal-close-btn").prop("disabled", disabled);
        };

        disableButtons(true);

        editCategory(categoryId, $("#edit-category-modal-category-name").val())
          .then((result) => {
            disableButtons(false);
            if (result.success) {
              bootstrap.Modal.getInstance($("#edit-category-modal").get(0)).hide();
              loadCategories(true);
            } else {
              $("#edit-category-error").prop("hidden", false).text(localizeToken(result.data.token));
            }
          });
      });
    }
  });

  $("#edit-category-modal").on("hide.bs.modal", () => {
    $("#edit-category-modal-save-btn").prop("disabled", true);
    $("#edit-category-modal-close-btn").prop("disabled", false);

    $("#edit-category-modal-category-name").off("keyup");
    $("#edit-category-form").off("submit");

    $("#edit-category-error").prop("hidden", true);
  });

  $("#remove-category-modal").on("show.bs.modal", (event) => {
    const button = $(event.relatedTarget);
    const categoryId = parseInt(button.attr("data-category"));
    const categoryName = categoriesById.get(categoryId);

    if (categoryName) {
      $("#remove-product-name").text(categoryName);

      $("#remove-category-modal-save-btn").on("click", (event) => {
        event.stopImmediatePropagation();

        $("#remove-category-modal-save-btn").prop("disabled", true);
        removeCategory(categoryId)
          .then((result) => {
            $("#remove-category-modal-save-btn").prop("disabled", false);

            if (result.success) {
              bootstrap.Modal.getInstance($("#remove-category-modal").get(0)).hide();
              loadCategories(true);
            } else {
              $("#remove-category-error").prop("hidden", false).text(localizeToken(result.data.token));
            }
          });
      });
    }
  });

  $("#remove-category-modal").on("hide.bs.modal", () => {
    $("#remove-category-modal-save-btn").off("click");
    $("#remove-category-modal-save-btn").prop("disabled", false);
    $("#remove-category-error").prop("hidden", true);
  });
}

const loadProducts = async () => {
  const result = await fetchProducts();

  if (result.success) {
    productsById = new Map(result.data.map(({ id, ...data }) => ([id, data])));

    $("#search-result")
      .empty()
      .append(
        ...result.data.map(product => buildProductItem(product)),
      );
  }
};

const configureProducts = async () => {
  await loadProducts();

  const validateProductName = (name) => name !== '' && name.length >= 4 && name.length <= 60;

  $("#search-product-name").on("keyup", () => {
    $("#search-product-btn").prop("disabled", !validateProductName($("#search-product-name").val()));
  });

  $("#search-product").on("submit", (event) => {
    event.preventDefault();
    event.stopImmediatePropagation();

    $("#search-product-error").prop("hidden", true);
    $("#search-product-btn").prop("disabled", true);

    const productName = $("#search-product-name").val();
    if (validateProductName(productName)) {
      findProductByName(productName)
        .then((result) => {
          $("#search-product-btn").prop("disabled", !validateProductName($("#search-product-name").val()));

          if (result.success) {
            $("#search-product-result")
              .empty()
              .append(...result.data.map(product => buildSearchProductItem(product)));
          } else {
            $("#search-product-error")
              .prop("hidden", false)
              .text(localizeToken(result.data.token));
          }
        });
    }
  });

  $("#add-product-modal").on("show.bs.modal", () => {
    $("#add-product-form").on("submit", (e) => {
      e.stopImmediatePropagation();
      e.preventDefault();

      const payload = Object.fromEntries($("#add-product-form")
        .serializeArray()
        .map(({ name, value }) => [name, value]));

      const toggleAddProductBtns = (disabled) => {
        $("#add-product-modal-close-btn").prop("disabled", disabled);
        $("#add-product-modal-save-btn").prop("disabled", disabled);
      };

      toggleAddProductBtns(true);
      addProduct(payload)
        .then((result) => {
          toggleAddProductBtns(false);
          
          if (result.success) {
            bootstrap.Modal.getInstance($("#add-product-modal").get(0)).hide();
            loadProducts();
          } else {
            $("#add-product-error").prop("hidden", false).text(localizeToken(result.data.token));
          }
        });
    });
  });

  $("#add-product-modal").on("hide.bs.modal", () => {
    $("#add-product-form").off("submit").find("input[type=text], input[type=number], input[type=url]").val("")
  });

  $("#edit-product-modal").on("show.bs.modal", (event) => {
    event.stopImmediatePropagation();

    const productId = parseInt($(event.relatedTarget).attr("data-product"));
    const product = productsById.get(productId);

    if (product) {
      $("#edit-product-modal-product-name").val(product.name);
      $("#edit-product-modal-product-description").val(product.description);
      $("#edit-product-modal-product-image").val(product.imageUrl);
      $("#edit-product-modal-product-price").val(product.price);
      $("#edit-product-modal-product-quantity").val(product.quantity);

      $("#edit-product-form").on("submit", (event) => {
        event.preventDefault();

        const payload = Object.fromEntries($("#edit-product-form")
          .serializeArray()
          .map(({ name, value }) => [name, value]));

        const toggleEditProductBtns = (disabled) => {
          $("#edit-product-modal-close-btn").prop("disabled", disabled);
          $("#edit-product-modal-save-btn").prop("disabled", disabled);
        };
        
        toggleEditProductBtns(true);
        editProduct(productId, payload)
          .then((result) => {
            toggleEditProductBtns(false);

            if (result.success) {
              loadProducts();
              $("#search-product-result").empty();
            }
          });
      });
    }
  });

  $("#edit-product-modal").on("hide.bs.modal", () => {
    $("#edit-product-form").off("submit");
  });
};

$(document).ready(async () => {
  await init();
  await configureCategories();
});

# Techlab E-Commerce

Se trata API básica que mediante **CRUD** permite ejecutar diferentes acciones en la base de datos. Se compone
de tres base paths: `/api/products`, `/api/categories` y `/api/orders`.
También se aprovecha el servidor Tomcat incluído en Springboot para servir una página sobre [localhost:8000](http://localhost:8000).
Dicha página muestra, de manera breve, los comportamientos de ciertos endpoints y cómo se podrían mostrar los productos y/o categorías.

---

## Formato de respuesta
Se le añadió una respuesta base estándar la cual usan **la mayoría de los controladores** y también **manejadores de errores**.
Los únicos que se exceptúan son aquellos controladores que respondan con el HTTP Status `204 No Content`.

### La estructura de la respuesta estándar

|Nombre de propiedad| Tipo    | Utlidad                                                            |
|-------------------|---------|--------------------------------------------------------------------|
| `success`| Boolean | Mediante `true`/`false` indica si la solicitud tuvo éxito o falló. 
| `message` | String  | Opcionalmente incluída. Detalla el HTTP Status Code.               |
| `data` | Object  | Opcionalmente incluído. Datos adicionales a la respuesta.          |

## Endpoints

| Método | Endpoint                                             | Acción                                                                                                                                                                                                          | Payload                                                                                            |
|--------|----------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| **GET** |`/api/products`                                     | Devuelve una lista de **todos los productos** disponibles.                                                                                                                                                      |                                                                                                    |
| **GET** |`/api/products/query?name=`                         | Devuelve una lista con **todos los productos** que coincidan con el nombre solicitado en el querystring.                                                                                                        |                                                                                                    |
| **GET**  |`/api/products/{id}`                                | Devuelve el producto solicitado, buscado por su id incluída en el path.                                                                                                                                         |                                                                                                    |
| **POST** |`/api/products/`                                   | Crea un producto.                                                                                                                                                                                               | { `name: String`, `description: String`, `price: Float`, `quantity: Integer`, `imageUrl: String` } |
| **PATCH** |`/api/products/{id}`                              | Edita la información de un producto especificado por su id en el path.<br/>Los campos a modificar son únicamente aquellos que fueron incluídos en el payload y cuyo nuevo valor sea diferente al ya almacenado. | { `name: String`, `description: String`, `price: Float`, `quantity: Integer`, `imageUrl: String` } |
| **DELETE** |`/api/products/{id}`                             | Elima un producto especificado por su id en el path.                                                                                                                                                            |                                                                                                    |
| **PUT** |`/api/products/{productId}/categories/{categoryId}` | Le añade una categoría _(categoryId)_ a un producto _(productId)_.                                                                                                                                              |                                                                                                    |
|**DELETE**|`/api/products/{productId}/categories/{categoryId}`| Elima una categoría _(categoryId)_ a un producto _(productId)_.                                                                                                                                                 |
|**GET**|`/api/categories`| Obtiene todas las categorías disponibles                                                                                                                                                                        |                                                                                                    |
|**GET**|`/api/categories/query?name=`| Devuelve una categorías que coincidan con el nombre incluído en el querystring.                                                                                                                                 |                                                                                                    |
|**GET**|`/api/categories/{id}`| Devuelve la categoría solicitada por su id en el path.                                                                                                                                                          |                                                                                                    |
|**POST**|`/api/categories`| Añadir una categoría                                                                                                                                                                                            | { `name: String` }                                                                                 |
|**PATCH**|`/api/categories/{id}`| Modifica una categoría por su id en el path.<br/>Se modfiican únicamente los campos que se hayan incluído en el payload y cuyo valor sea diferente al almacenado.                                               | { `name: String` }                                                                                 |
|**DELETE**|`/api/categories/{id}`| Elimina una categoría por su id incluida en el path.                                                                                                                                                            |
|**GET**|`/api/orders`| Devuelve todos los pedidos disponibles.<br/>**No se incluyen los productos de cada pedido**.                                                                                                                    |                                                                                                    |
|**GET**|`/api/orders/{id}`| Devuelve el pedido especificado por su id. Incluye todos los pedidos con información de cada uno.                                                                                                               |                                                                                                    |
|**POST**|`/api/orders`| Crea una orden vacía y devuelve la información de la misma.                                                                                                                                                     |                                                                                                    ||
|**PATCH**|`/api/orders/{id}/confirm`| Confirma la orden, esto significa que resta las cantidades solicitadas del stock total disponible.                                                                                                              
|**DELETE**|`/api/orders/{id}`| Elimina la orden por su id en el path.                                                                                                                                                                          |                                                                                                    |
|**PUT**|`/api/orders/{orderId}/products/{productId}`|Añade la cantidad de un producto _(productId)_ a la orden _(orderId)_.| { `quantity: Integer` }                                                                            |
|**PATCH**|`/api/orders/{orderId}/products/{productId}`|Modifica la cantidad de un product _(productId)_ en una orden _(orderId)_.| { `quantity: Integer` }                                                                            |
|**DELETE**|`/api/orders/{orderId}/products/{productId}`|Elimina un producto _(productId)_ de una orden _(orderId)_.||

## Requerimientos
* Base de datos MySQL
* JVM 21

## ¿Cómo deployearlo?
1. Descargar el último [lanzamiento](https://github.com/zaykkko/ttech-final-backend/releases) y extraerlo.
2. Crear una base de datos `ttech` e importar `ttech_dump.sql`.
3. Crear un archivo `.env` en el directorio principal y definir las variables `DATABASE_URL`, `DATABASE_USERNAME` y `DATABASE_PASSWORD`.
4. Encenderlo mediante `java -jar ./build/ttech-final-backend-1.0.x.jar`.
5. Acceder a través de [http://localhost:8000](http://localhost:8000) (en caso de que el puerto no esté siendo utilizado).

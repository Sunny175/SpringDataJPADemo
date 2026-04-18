/**
 * The base URL for the product API endpoints.
 * @constant {string}
 */
const API_BASE_URL = '/api/products';

/**
 * Handles API responses, throwing formatted errors for Bad Requests (e.g., validation errors).
 * @param {Response} response - The fetch response object
 * @returns {Promise<any>} The parsed JSON response
 */
async function handleResponse(response) {
    if (!response.ok) {
        if (response.status === 400) {
            // Backend sends map of field errors, extracting them to forward to the UI
            const errors = await response.json();
            throw { status: 400, validationErrors: errors };
        }
        throw new Error('An unexpected error occurred: ' + response.statusText);
    }
    // Delete returns empty OK which throws JSON parse error if we aren't careful
    const text = await response.text();
    return text ? JSON.parse(text) : {};
}

/**
 * Fetches all products from the backend.
 * @returns {Promise<Array>} A promise that resolves to an array of products.
 */
export async function getProducts() {
    const response = await fetch(API_BASE_URL);
    return handleResponse(response);
}

/**
 * Fetches a single product by its ID.
 * @param {number} id - The ID of the product to fetch.
 * @returns {Promise<Object>} A promise that resolves to the product object.
 */
export async function getProductById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    return handleResponse(response);
}

/**
 * Adds a new product.
 * @param {Object} product - The product data to add.
 * @returns {Promise<Object>} A promise that resolves to the added product.
 */
export async function addProduct(product) {
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
    });
    return handleResponse(response);
}

/**
 * Updates an existing product.
 * @param {Object} product - The updated product data.
 * @returns {Promise<Object>} A promise that resolves to the updated product.
 */
export async function updateProduct(product) {
    const response = await fetch(`${API_BASE_URL}/${product.productId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
    });
    return handleResponse(response);
}

/**
 * Deletes a product by its ID.
 * @param {number} id - The ID of the product to delete.
 * @returns {Promise<string>} A promise indicating completion.
 */
export async function deleteProduct(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete product');
    return response.text();
}

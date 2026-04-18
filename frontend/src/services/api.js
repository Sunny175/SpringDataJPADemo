const API_BASE_URL = '/api/products';

export async function getProducts() {
    const response = await fetch(API_BASE_URL);
    if (!response.ok) throw new Error('Failed to fetch products');
    return response.json();
}

export async function getProductById(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    if (!response.ok) throw new Error('Failed to fetch product');
    return response.json();
}

export async function addProduct(product) {
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
    });
    if (!response.ok) throw new Error('Failed to add product');
    return response.json();
}

export async function updateProduct(product) {
    const response = await fetch(`${API_BASE_URL}/${product.productId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
    });
    if (!response.ok) throw new Error('Failed to update product');
    return response.json();
}

export async function deleteProduct(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete product');
    // Backend returns string, not json for delete. Might need to read as text.
    return response.text();
}

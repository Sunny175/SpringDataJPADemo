import { useState, useEffect } from 'react';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import * as api from './services/api';

/**
 * Main application component that manages state and layout.
 * @returns {JSX.Element} The rendered App component.
 */
function App() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Modal State
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  /**
   * Fetches all products on initial component mount.
   */
  useEffect(() => {
    fetchProducts();
  }, []);

  /**
   * Wrapper function to handle requesting products from API.
   * Updates loading and error states automatically.
   */
  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await api.getProducts();
      setProducts(data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch products. Is the Spring Boot backend running?');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Opens the form modal in "Add" mode.
   */
  const handleAddClick = () => {
    setEditingProduct(null);
    setIsFormOpen(true);
  };

  /**
   * Opens the form modal in "Edit" mode with the selected product.
   * @param {Object} product - The product to edit.
   */
  const handleEditClick = (product) => {
    setEditingProduct(product);
    setIsFormOpen(true);
  };

  /**
   * Closes the form modal.
   */
  const handleCloseForm = () => {
    setIsFormOpen(false);
    setEditingProduct(null);
  };

  /**
   * Handles saving product (supports both create and update operations).
   * @param {Object} productData - The data payload to save.
   * @returns {Promise<Object>} An object containing validation errors if the save failed, else null.
   */
  const handleSaveProduct = async (productData) => {
    try {
      if (editingProduct) {
        const updated = await api.updateProduct(productData);
        setProducts(products.map(p => p.productId === updated.productId ? updated : p));
      } else {
        const added = await api.addProduct(productData);
        setProducts([...products, added]);
      }
      handleCloseForm();
      return null; // Return null if successful
    } catch (err) {
      if (err.status === 400 && err.validationErrors) {
        return err.validationErrors; // Return backend validation map to the form
      }
      console.error(err);
      alert('Failed to save product. Please try again.');
      return null;
    }
  };

  /**
   * Hands deleting a specific product by its ID.
   * @param {number} id - The ID of the product.
   */
  const handleDeleteProduct = async (id) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await api.deleteProduct(id);
        setProducts(products.filter(p => p.productId !== id));
      } catch (err) {
        console.error(err);
        alert('Failed to delete product.');
      }
    }
  };

  return (
    <div className="app-container">
      <header className="header glass-panel">
        <h1>E-Commerce Admin</h1>
        <button className="btn btn-primary" onClick={handleAddClick}>
          + Add Product
        </button>
      </header>

      {error && (
        <div className="glass-panel" style={{ color: 'var(--danger)', padding: '1rem', marginBottom: '2rem', borderLeft: '4px solid var(--danger)' }}>
          {error}
        </div>
      )}

      {loading ? (
        <div className="loader">Loading products...</div>
      ) : (
        <ProductList 
          products={products} 
          onEdit={handleEditClick} 
          onDelete={handleDeleteProduct} 
        />
      )}

      {isFormOpen && (
        <ProductForm 
          product={editingProduct} 
          onSave={handleSaveProduct} 
          onClose={handleCloseForm} 
        />
      )}
    </div>
  );
}

export default App;

import { useState, useEffect } from 'react';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import * as api from './services/api';

/**
 * Main application component that manages state, filters, pagination and layout.
 * @returns {JSX.Element} The rendered App component.
 */
function App() {
  // Data States
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Search and Pagination States
  const [keyword, setKeyword] = useState('');
  const [searchQuery, setSearchQuery] = useState(''); // To trigger fetch only when user submits search
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Modal State
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  /**
   * Fetches products whenever the requested page or search query changes.
   */
  useEffect(() => {
    fetchProducts();
  }, [page, searchQuery]);

  /**
   * Wrapper function to handle requesting products from API.
   * Expects a Page object from Spring Boot.
   */
  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await api.getProducts(searchQuery, page);
      // Spring Data JPA wraps the list inside of "content"
      setProducts(data.content || []);
      setTotalPages(data.totalPages || 0);
      setError(null);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch products. Is the Spring Boot backend running?');
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Triggers a search based on the keyword input.
   */
  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0); // Reset to first page on a new search
    setSearchQuery(keyword); 
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
   * Reloads the current page on save to ensure data sync.
   * @param {Object} productData - The data payload to save.
   */
  const handleSaveProduct = async (productData) => {
    try {
      if (editingProduct) {
        await api.updateProduct(productData);
      } else {
        await api.addProduct(productData);
      }
      handleCloseForm();
      fetchProducts(); // Refresh to maintain correct pagination & search order
      return null;
    } catch (err) {
      if (err.status === 400 && err.validationErrors) {
        return err.validationErrors;
      }
      console.error(err);
      alert('Failed to save product. Please try again.');
      return null;
    }
  };

  /**
   * Hands deleting a specific product by its ID.
   * Reloads data.
   */
  const handleDeleteProduct = async (id) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await api.deleteProduct(id);
        fetchProducts(); // Refresh list to get accurate page size
      } catch (err) {
        console.error(err);
        alert('Failed to delete product.');
      }
    }
  };

  return (
    <div className="app-container">
      <header className="header glass-panel" style={{ flexWrap: 'wrap', gap: '1rem' }}>
        <h1 style={{ margin: 0 }}>E-Commerce Admin</h1>
        
        {/* Search Bar UI */}
        <form onSubmit={handleSearch} style={{ display: 'flex', gap: '0.5rem', flex: '1', minWidth: '250px' }}>
          <input 
            type="text" 
            className="form-control" 
            placeholder="Search products by name..." 
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            style={{ margin: 0 }}
          />
          <button type="submit" className="btn btn-outline">Search</button>
          {(searchQuery || keyword) && (
             <button type="button" className="btn btn-outline" style={{ border: 'none', color: 'var(--danger)' }} onClick={() => { setKeyword(''); setSearchQuery(''); setPage(0); }}>
               Clear
             </button>
          )}
        </form>

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
        <>
          <ProductList 
            products={products} 
            onEdit={handleEditClick} 
            onDelete={handleDeleteProduct} 
          />

          {/* Pagination Controls */}
          {totalPages > 1 && (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '1rem', marginTop: '2rem' }}>
              <button 
                className="btn btn-outline" 
                disabled={page === 0} 
                onClick={() => setPage(page - 1)}
              >
                Previous
              </button>
              
              <span style={{ color: 'var(--text-secondary)' }}>
                Page {page + 1} of {totalPages}
              </span>
              
              <button 
                className="btn btn-outline" 
                disabled={page >= totalPages - 1} 
                onClick={() => setPage(page + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
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

import { useState, useEffect } from 'react';

export default function ProductForm({ product, onSave, onClose }) {
  const [formData, setFormData] = useState({
    productId: '',
    productName: '',
    productPrice: ''
  });

  const isEdit = !!product;

  useEffect(() => {
    if (product) {
      setFormData({
        productId: product.productId,
        productName: product.productName,
        productPrice: product.productPrice
      });
    }
  }, [product]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      productId: parseInt(formData.productId, 10),
      productName: formData.productName,
      productPrice: parseInt(formData.productPrice, 10)
    });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content glass-panel">
        <h2>{isEdit ? 'Edit Product' : 'Add New Product'}</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '1.5rem' }}>
          
          <div className="form-group">
            <label htmlFor="productId">Product ID</label>
            <input 
              type="number"
              id="productId"
              name="productId"
              className="form-control"
              value={formData.productId}
              onChange={handleChange}
              disabled={isEdit} 
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="productName">Product Name</label>
            <input 
              type="text"
              id="productName"
              name="productName"
              className="form-control"
              value={formData.productName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="productPrice">Price</label>
            <input 
              type="number"
              id="productPrice"
              name="productPrice"
              className="form-control"
              value={formData.productPrice}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-actions">
            <button type="button" className="btn btn-outline" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              {isEdit ? 'Save Changes' : 'Add Product'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

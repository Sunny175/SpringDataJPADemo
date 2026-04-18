import { useState, useEffect } from 'react';

/**
 * A modal component displaying a form to add or edit a product.
 * @param {Object} props
 * @param {Object} [props.product] - The product to edit, or null/undefined if creating a new product.
 * @param {Function} props.onSave - Callback triggered on form submission. Expects to return validation errors if any. 
 * @param {Function} props.onClose - Callback triggered to close the modal.
 * @returns {JSX.Element} The rendered ProductForm component.
 */
export default function ProductForm({ product, onSave, onClose }) {
  const [formData, setFormData] = useState({
    productId: '',
    productName: '',
    productPrice: ''
  });
  
  // State to hold and display backend validation error messages gracefully
  const [errors, setErrors] = useState({});

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

  /**
   * Updates state based on input change.
   * Also clears individual validation error once a user starts typing.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear the error for this field dynamically as they type
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  /**
   * Submits the product data and handles backend validation signals.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({}); // Reset errors

    const payload = {
      productName: formData.productName,
      productPrice: parseInt(formData.productPrice, 10)
    };
    
    // Only pass ID if we are explicitly editing an existing item
    if (isEdit && formData.productId) {
      payload.productId = parseInt(formData.productId, 10);
    }

    const validationErrors = await onSave(payload);

    // If the onSave returned a dictionary of errors from the backend, bind them to state
    if (validationErrors) {
      setErrors(validationErrors);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content glass-panel">
        <h2>{isEdit ? 'Edit Product' : 'Add New Product'}</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '1.5rem' }}>
          
          <div className="form-group">
            <label htmlFor="productId">Product ID</label>
            <input 
              type="text"
              id="productId"
              name="productId"
              className="form-control"
              value={isEdit ? formData.productId : ''}
              disabled={true} 
              placeholder={isEdit ? '' : 'Auto-generated on Add'}
            />
            {errors.productId && <span style={{ color: 'var(--danger)', fontSize: '0.85rem' }}>{errors.productId}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="productName">Product Name</label>
            <input 
              type="text"
              id="productName"
              name="productName"
              className={`form-control ${errors.productName ? 'input-error' : ''}`}
              value={formData.productName}
              onChange={handleChange}
              required
            />
            {errors.productName && <span style={{ color: 'var(--danger)', fontSize: '0.85rem' }}>{errors.productName}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="productPrice">Price</label>
            <input 
              type="number"
              id="productPrice"
              name="productPrice"
              className={`form-control ${errors.productPrice ? 'input-error' : ''}`}
              value={formData.productPrice}
              onChange={handleChange}
              required
            />
            {errors.productPrice && <span style={{ color: 'var(--danger)', fontSize: '0.85rem' }}>{errors.productPrice}</span>}
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

export default function ProductCard({ product, onEdit, onDelete }) {
  return (
    <div className="product-card glass-panel">
      <div className="product-info">
        <h2>{product.productName}</h2>
        <div className="product-price">${product.productPrice}</div>
        <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem', marginTop: '0.5rem' }}>
          ID: {product.productId}
        </div>
      </div>
      <div className="product-actions">
        <button 
          className="btn btn-outline" 
          style={{ flex: 1 }} 
          onClick={() => onEdit(product)}
        >
          Edit
        </button>
        <button 
          className="btn btn-danger" 
          onClick={() => onDelete(product.productId)}
        >
          Delete
        </button>
      </div>
    </div>
  );
}

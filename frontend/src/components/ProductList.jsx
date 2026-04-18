import ProductCard from './ProductCard';

export default function ProductList({ products, onEdit, onDelete }) {
  if (!products || products.length === 0) {
    return (
      <div className="empty-state glass-panel">
        <h2 style={{ marginBottom: '1rem', color: 'var(--text-primary)' }}>No Products Found</h2>
        <p>Get started by adding a new product to your inventory.</p>
      </div>
    );
  }

  return (
    <div className="product-grid">
      {products.map(product => (
        <ProductCard 
          key={product.productId} 
          product={product} 
          onEdit={onEdit}
          onDelete={onDelete}
        />
      ))}
    </div>
  );
}

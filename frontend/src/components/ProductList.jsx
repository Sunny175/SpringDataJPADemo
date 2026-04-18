import ProductCard from './ProductCard';

/**
 * A layout component that handles mapping an array of products into a visual grid of cards.
 * @param {Object} props
 * @param {Array} props.products - An array of product objects retrieved from the API.
 * @param {Function} props.onEdit - Forwarded callback to handle editing a product.
 * @param {Function} props.onDelete - Forwarded callback to handle deleting a product.
 * @returns {JSX.Element} The rendered product grid or an empty state indicator.
 */
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

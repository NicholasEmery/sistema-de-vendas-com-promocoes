import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                // Note: You'll need a valid JWT token for this request.
                // This is a placeholder and will fail without a real token.
                const token = 'your_jwt_token';
                const response = await axios.get('http://localhost:8080/api/products', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setProducts(response.data);
            } catch (err) {
                setError('Failed to fetch products. Please make sure the backend is running and you have a valid token.');
                console.error(err);
            }
        };

        fetchProducts();
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1>Product List</h1>
            <ul>
                {products.map(product => (
                    <li key={product.id}>{product.name} - ${product.price}</li>
                ))}
            </ul>
        </div>
    );
};

export default ProductList;

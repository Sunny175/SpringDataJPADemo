import requests
import random
import time

API_URL = "http://localhost:8080/api/products"
NUM_RECORDS = 10000

# Sample word lists for realistic product names
adjectives = ["Super", "Ultra", "Mega", "Smart", "Sleek", "Pro", "Lite", "Basic", "Advanced", "Premium", "Wireless", "Gaming"]
nouns = ["Widget", "Gadget", "Device", "Tool", "Phone", "Laptop", "Monitor", "Keyboard", "Mouse", "Speaker", "Headphones", "Tablet"]

def generate_random_product():
    """Generates a random product payload."""
    name = f"{random.choice(adjectives)} {random.choice(nouns)} {random.randint(100, 999)}"
    price = random.randint(10, 5000)  # Price between 10 and 5000
    return {
        "productName": name,
        "productPrice": price
    }

def seed_database():
    """Loops and hits the add product API to insert data."""
    print(f"Starting to seed {NUM_RECORDS} products into the database via {API_URL}...")
    success_count = 0
    fail_count = 0
    
    start_time = time.time()
    
    # We will use a session for connection pooling which drastically speeds up requests
    session = requests.Session()
    
    for i in range(NUM_RECORDS):
        product_data = generate_random_product()
        try:
            response = session.post(API_URL, json=product_data)
            # Spring Boot typically returns 200 OK or 201 Created on success
            if response.status_code in [200, 201]:
                success_count += 1
            else:
                print(f"Failed to insert '{product_data['productName']}': Status Code {response.status_code} - {response.text}")
                fail_count += 1
        except requests.exceptions.RequestException as e:
            print(f"Connection error: {e}")
            fail_count += 1
            break # Stop if connection fails entirely (e.g. server is down)
            
        if (i + 1) % 100 == 0:
            print(f"Processed {i + 1}/{NUM_RECORDS} requests...")
            
    end_time = time.time()
    print(f"\nDone! Successfully added: {success_count}, Failed: {fail_count}")
    print(f"Time taken: {end_time - start_time:.2f} seconds")

if __name__ == "__main__":
    seed_database()

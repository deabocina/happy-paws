CREATE TABLE "user" (
    id UUID PRIMARY KEY NOT NULL,
    role VARCHAR(20) NOT NULL,
    name VARCHAR(30),
    surname VARCHAR(30),
    gender VARCHAR(10),
    date_of_birth DATE,
    email VARCHAR(40),
    phone_number VARCHAR(15),
    password VARCHAR(200),
    city VARCHAR(20),
    address VARCHAR(30)
);

CREATE TABLE brand (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE product (
    id UUID PRIMARY KEY NOT NULL,
    stock_quantity INT,
    product_image VARCHAR(255),
    name VARCHAR(80) NOT NULL,
    pet_type VARCHAR(20),
    category VARCHAR(40),
    subcategory VARCHAR(40),
    brand_id UUID,
    price DECIMAL(6, 2) NOT NULL,
    current_rating DECIMAL(2, 1),
    tax_amount INT,
    number_of_buyers INT,
    FOREIGN KEY (brand_id) REFERENCES brand(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE "order" (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID,
    total_price DECIMAL(7, 2),
    delivery_option VARCHAR(30),
    order_date DATE,
    order_status VARCHAR(20),
    user_rating INT,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE order_item (
    id UUID PRIMARY KEY NOT NULL,
    order_id UUID,
    product_id UUID,
    quantity INT,
    price_per_item DECIMAL(6, 2),
    FOREIGN KEY (order_id) REFERENCES "order"(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE pet_profile (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID,
    pet_image VARCHAR(255),
    pet_name VARCHAR(30),
    pet_type VARCHAR(20),
    birth_date DATE,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE wishlist (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID,
    product_id UUID,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE discount (
    id UUID PRIMARY KEY NOT NULL,
    discount_amount INT NOT NULL,
    pet_profile_id UUID,
    user_id UUID,
    brand_id UUID,
    original_price DECIMAL(6, 2),
    product_id UUID,
    FOREIGN KEY (pet_profile_id) REFERENCES pet_profile(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (brand_id) REFERENCES brand(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    FOREIGN KEY (product_id) REFERENCES product(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS sales (
  sales_id integer NOT NULL,
  amount integer NOT NULL,
  status CHAR(1) NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (sales_id)
);
CREATE TABLE IF NOT EXISTS dist_sales (
  sales_id integer NOT NULL,
  amount integer NOT NULL,
  status CHAR(1) NOT NULL,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (sales_id)
);
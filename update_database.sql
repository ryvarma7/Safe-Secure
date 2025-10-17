-- Add new columns to requests table
ALTER TABLE requests 
ADD COLUMN expiry_time TIMESTAMP NULL,
ADD COLUMN previous_request_id INT NULL,
MODIFY COLUMN status ENUM('pending', 'approved', 'rejected', 'expired') NOT NULL DEFAULT 'pending';

-- Add foreign key for previous_request_id
ALTER TABLE requests
ADD CONSTRAINT fk_previous_request 
FOREIGN KEY (previous_request_id) REFERENCES requests(id) ON DELETE SET NULL;
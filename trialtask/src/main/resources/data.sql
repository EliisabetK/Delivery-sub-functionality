INSERT INTO base_fee (id, city, fee, vehicle_type) VALUES
    (1, 'Tallinn', 4, 'Car'),
    (2, 'Tallinn', 3.5, 'Scooter'),
    (3, 'Tallinn', 3, 'Bike'),
    (4, 'Tartu', 3.5, 'Car'),
    (5, 'Tartu', 3, 'Scooter'),
    (6, 'Tartu', 2.5, 'Bike'),
    (7, 'Pärnu', 3, 'Car'),
    (8, 'Pärnu', 2.5, 'Scooter'),
    (9, 'Pärnu', 2, 'Bike');

INSERT INTO extra_fees (id, condition_type, condition_value, extra_fee, vehicle_type) VALUES
    (1, 'Air Temperature', '< -10', 1, 'Scooter'),
    (2, 'Air Temperature', '< -10', 1, 'Bike'),
    (3, 'Air Temperature', '>= -10 and < 0', 0.5, 'Scooter'),
    (4, 'Air Temperature', '>= -10 and < 0', 0.5, 'Bike'),
    (5, 'Wind Speed', '>= 10 and < 20', 0.5, 'Bike'),
    (6, 'Weather Phenomenon', 'Snow or Sleet', 1, 'Scooter'),
    (7, 'Weather Phenomenon', 'Snow or Sleet', 1, 'Bike'),
    (8, 'Weather Phenomenon', 'Rain', 0.5, 'Scooter'),
    (9, 'Weather Phenomenon', 'Rain', 0.5, 'Bike');
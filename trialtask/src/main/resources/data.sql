-- In case the database is not populated, these scripts can be run to populate the tables with business rules.

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

INSERT INTO ExtraFee (ID, CONDITION_TYPE, CONDITION_VALUE, EXTRA_FEE, VEHICLE_TYPE) VALUES
    (1, 'Air Temperature', '< -10', 1.0, 'Bike'),
    (2, 'Air Temperature', '>= -10 and < 0', 0.5, 'Scooter'),
    (3, 'Air Temperature', '>= -10 and < 0', 0.5, 'Bike'),
    (4, 'Wind Speed', '>= 10 and < 20', 0.5, 'Bike'),
    (5, 'Weather Phenomenon', 'Snow or Sleet', 1.0, 'Scooter'),
    (6, 'Weather Phenomenon', 'Snow or Sleet', 1.0, 'Bike'),
    (7, 'Weather Phenomenon', 'Rain', 0.5, 'Scooter'),
    (8, 'Weather Phenomenon', 'Rain', 0.5, 'Bike'),
    (9, 'Wind Speed', '> 20', -1000.0, 'Bike'),
    (10, 'Weather Phenomenon', 'Glaze, Hail, or Thunder', -1000.0, 'Scooter'),
    (11, 'Weather Phenomenon', 'Glaze, Hail, or Thunder', -1000.0, 'Bike');

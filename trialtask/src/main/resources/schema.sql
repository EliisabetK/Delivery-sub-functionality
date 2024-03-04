CREATE TABLE weather (
     id INT PRIMARY KEY,
     station_name VARCHAR(255),
     wmo_code INT,
     air_temperature DECIMAL(5, 2),
     wind_speed DECIMAL(5, 2),
     weather_phenomenon VARCHAR(255),
     observation_timestamp TIMESTAMP
);

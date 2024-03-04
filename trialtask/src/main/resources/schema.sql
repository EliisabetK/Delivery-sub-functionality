CREATE TABLE weather_data (
     id INT PRIMARY KEY,
     station_name VARCHAR(255),
     wmocode INT,
     air_temperature DECIMAL(5, 2),
     wind_speed DECIMAL(5, 2),
     phenomenon VARCHAR(255),
     observation_timestamp TIMESTAMP
);

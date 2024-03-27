# Use an official Python runtime as the base image
FROM tiangolo/uwsgi-nginx-flask:python3.8-alpine
ENV STATIC_URL /static

# Set the working directory in the container
WORKDIR /app


# Copy the requirements file into the container
COPY requirements.txt .

# Install the app's dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the app's code into the container
COPY . .

# Expose the port that the app will run on
EXPOSE 80

# Run the command to start the app
CMD ["flask","--app", "app","run","--host=0.0.0.0", "--port=80"]


print("Starting mongo-init.js script...");

db = db.getSiblingDB('admin');

print("Attempting to create a new user...");

try {
    db.createUser({
        user: "mithilesh",
        pwd: "rootpassword",
        roles: [{ role: "userAdminAnyDatabase", db: "admin" },
                { role: "readWriteAnyDatabase", db: "admin" }]
    });
    print("User 'mithilesh' successfully created!");
    db.users.insertOne({ username: "admin",
        password: "$2y$10$MLR3fASRuicQ/hPmhLXEquGxvYc8jNZAbwJ3WZueEwG1KYVnPH.7O",
        userRoles: [
            "ROLE_USER",
            "ROLE_ADMIN"
        ] });
} catch (err) {
    print("Error creating user: " + err);
}

print("mongo-init.js script execution completed.");
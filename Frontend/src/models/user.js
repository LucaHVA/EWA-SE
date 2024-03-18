export class User{
    id;
    username;
    email;
    password;

    constructor(id, username, email, password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    static copyConstructor(user) {
        if (user === null || user === undefined) return null;
        return Object.assign(new User(0), user);
    }
}
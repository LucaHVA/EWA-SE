class User{
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

    static copyConstructor(user){
        if (user==null) return null;

        let copy=new User(
            user.id,
            user.username,
        user.email,
        user.password,
        )
    }
}
export class Friend {
    id;
    user;
    friend;
    status;

    constructor(id, user, friend, status) {
        this.id = id;
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    static copyConstructor(friend) {
        if (friend === null || friend === undefined) return null;
        return Object.assign(new Friend(0), friend);
    }
}
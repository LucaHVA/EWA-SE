import {fetch} from "whatwg-fetch";
import {User} from "@/models/user";
import {GameHistory} from "@/models/gameHistory";

export class UsersAdaptor {
    resourcesUrl;
    BROWSER_STORAGE_ITEM_NAME;
    _currentToken;
    _currentUser;

    constructor(resourcesUrl, browserStorageItemName) {
        this.resourcesUrl = resourcesUrl;
        this.BROWSER_STORAGE_ITEM_NAME = browserStorageItemName;
        this._currentUser = null;
        this._currentToken = null;
        this.getTokenFromBrowserStorage();
    }

    get getCurrentUser() {
        return this._currentUser;
    }

    set currentUser(value) {
        this._currentUser = value;
    }

    get currentToken() {
        return this._currentToken;
    }

    set currentToken(value) {
        this._currentToken = value;
    }

    async fetchJson(url, options = null) {
        let res = await fetch(url, options);
        if (res.ok && res.headers.get('content-type')?.includes('application/json')) {
            return await res.json();
        } else {
            console.log(res, !res.bodyUsed ? await res.text() : "");
            return null;
        }
    }

    async asyncFindAll() {
        try {
            const users = await this.fetchJson(this.resourcesUrl + "/all", {
                method: 'GET'
            });
            return users ? users.map(User.copyConstructor) : [];  // Check for null and return an empty array if needed
        } catch (e) {
            console.log(e);
            return [];
        }
    }

    async asyncFindById(id) {
        console.log(id);
        return await this.fetchJson(`${this.resourcesUrl}/${id}`);
    }

    async save(user, queryParams) {
        try {
            if (user.id === 0) {
                const url = `${this.resourcesUrl}${queryParams ? `?${queryParams}` : ''}`;
                const options = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(user)
                };

                const response = await this.fetchJson(url, options);

                if (response) {
                    return User.copyConstructor(response);
                } else {
                    console.error('Failed to save user.');
                    return null;
                }
            }

            const createdUser = User.copyConstructor(user);
            const res = await this.fetchJson(this.resourcesUrl + "/" + user.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(createdUser)
            });
            return User.copyConstructor(res);
        } catch (error) {
            console.error('Error during save:', error);
            return null;
        }
    }

    async login(username, password) {
        try {
            const url = `${this.resourcesUrl}/login`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username, password}),
                credentials: "include"
            };
            const response = await fetch(url, options);
            if (response.ok) {
                const user = await response.json();
                const token = response.headers.get('Authorization');
                this.saveTokenIntoBrowserStorage(token, user);
                return user; // Return the logged-in user
            } else {
                console.error('Login failed: Invalid username or password.');
                return null;
            }
        } catch (error) {
            console.error('Error during login:', error);
            return null;
        }
    }



    getTokenFromBrowserStorage() {
        this._currentToken = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME);
        const jsonUser = window.sessionStorage.getItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");

        if (jsonUser) {
            this._currentUser = JSON.parse(jsonUser);
        } else {
            this._currentUser = null;
        }

        return this._currentToken;
    }

    get currentUser() {
        if (!this._currentUser) {
            this.getTokenFromBrowserStorage();
        }
        return this._currentUser;
    }

    isAuthenticated() {
        return this.currentUser != null;
    }

    signOut() {
        this.saveTokenIntoBrowserStorage(null, null);
    }

    saveTokenIntoBrowserStorage(token, user) {
        this._currentToken = token;
        this._currentUser = user;

        // console.log('Saving token into storage:', token);
        // console.log('Saving user JSON into storage:', JSON.stringify(user));
        //FIXME remove password from being saved in browser storage for security reason

        if (token == null) {
            this._currentUser = null;
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME);
            window.sessionStorage.removeItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC");
        } else {
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME, token);
            window.sessionStorage.setItem(this.BROWSER_STORAGE_ITEM_NAME + "_ACC", JSON.stringify(user));
        }
    }



    async fetchMatchHistory(userId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/games`;
            const options = {
                method: 'GET',
                headers: {
                    'Authorization': this.currentToken
                }
            };
            const response = await this.fetchJson(url, options);
            if (response) {
                const matchHistory = await response.json();

                return matchHistory.map(GameHistory.copyConstructor);
            } else {
                console.error('Failed to fetch match history.');
                return null;
            }
        } catch (error) {
            console.error('Error during fetch match history:', error);
            return null;
        }
    }


}
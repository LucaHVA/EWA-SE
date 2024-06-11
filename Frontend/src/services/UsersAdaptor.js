import {fetch} from "whatwg-fetch";
import {User} from "@/models/user";
import {GameHistory} from "@/models/gameHistory";
import {Friend} from "@/models/friend";

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

    //The [] is used as a fallback value to handle cases where the expected data might be null or undefined remember noobs.
    async asyncFindAll() {
        try {
            const users = await this.fetchJson(this.resourcesUrl + "/all", {
                method: "GET",
            });
            if (users) {
                for (let user of users) {
                    user.points = await this.calculatePlayerPoints(user.id);
                }
                return users.map(User.copyConstructor);
            } else {
                return [];
            }
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
                user.roles = ["USER"];
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
            console.log(response)
            if (response.ok) {
                const user = await response.json();
                this.saveTokenIntoBrowserStorage(response.headers.get('Authorization'), user);
                return response;
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
            //URL for fetching the match history using the user ID
            const url = `${this.resourcesUrl}/${userId}/games`;
            const options = {
                method: "GET",
            };
            // Send the fetch request and await the response
            const response = await this.fetchJson(url, options);
            // If a response is received, map the JSON data to GameHistory objects using the copyConstructor static method
            //true or false
            //to each element of the array
            return response ? response.map(GameHistory.copyConstructor) : [];
        } catch (error) {
            console.error("Error during fetch match history:", error);
            return [];
        }
    }

    async saveGameHistories(userId, gameHistories) {
        try {
            const url = `${this.resourcesUrl}/${userId}/saveGameHistories`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(gameHistories)
            };

            const response = await this.fetchJson(url, options);
            if (!response) {
                throw new Error('Failed to save game histories');
            }

            return response.map(GameHistory.copyConstructor);
        } catch (error) {
            console.error('Error during save game histories:', error);
            return null;
        }
    }

    async calculatePlayerPoints(userId) {
        const matchHistory = await this.fetchMatchHistory(userId);
        let playerPoints = 0;
        for (const match of matchHistory) {
            switch (match.placement) {
                case 1:
                    playerPoints += 5;
                    break;
                case 2:
                    playerPoints += 3;
                    break;
                case 3:
                    playerPoints += 1;
                    break;
                default:
                    break;
            }
        }
        return playerPoints;
    }

    async getFriends(userId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/friends`;
            const options = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            return await this.fetchJson(url, options);
        } catch (error) {
            console.error('Error fetching friends:', error);
            return null;
        }
    }

    async getFriendRequests(userId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/friendRequests`;
            const options = {
                method: 'GET'
            };
            return await this.fetchJson(url, options);
        } catch (error) {
            console.error('Error fetching friend requests:', error);
            return null;
        }
    }

    async getSentFriendRequests(userId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/sentFriendRequests`;
            const options = {
                method: 'GET'
            };
            return await this.fetchJson(url, options);
        } catch (error) {
            console.error('Error fetching sent friend requests:', error);
            return null;
        }
    }

    async sendFriendRequest(userId, friendId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/friends/${friendId}`;
            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            const response = await this.fetchJson(url, options);
            if (!response) {
                throw new Error('Failed to send friend request');
            }
            return Friend.copyConstructor(response);
        } catch (error) {
            console.error('Error sending friend request:', error);
            return null;
        }
    }

    async acceptFriendRequest(userId, requestId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/friends/${requestId}/accept`;
            const options = {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            return await this.fetchJson(url, options);
        } catch (error) {
            console.error('Error accepting friend request:', error);
            return null;
        }
    }

    async declineFriendRequest(userId, requestId) {
        try {
            const url = `${this.resourcesUrl}/${userId}/friends/${requestId}/decline`;
            const options = {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            return await this.fetchJson(url, options);
        } catch (error) {
            console.error('Error declining friend request:', error);
            return null;
        }
    }

    async deleteUser(userId) {
        try {
            const url = `${this.resourcesUrl}/${userId}`;
            const options = {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': this.currentToken
                }
            };
            const response = await fetch(url, options);
            if (response.ok) {
                return response;
            } else {
                console.error('Failed to delete user:', response.statusText);
                return null;
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            return null;
        }
    }
}
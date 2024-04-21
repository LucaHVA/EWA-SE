import {User} from "@/models/user";


export class UsersAdaptor{
    resourcesUrl;

    constructor(resourcesUrl) {
        this.resourcesUrl=resourcesUrl;
    }

    async fetchJson(url, options = null) {
        let res = await fetch(url, options);
        if (res.ok && res.headers.get('content-type')?.includes('application/json')) {
            return res.json();
        } else {
            console.log(res, !res.bodyUsed ? await res.text() : "")
            return null;
        }
    }

    async asyncFindAll() {
        try {
            const scooters = await this.fetchJson(this.resourcesUrl + "/all", {
                method: 'GET'
            })
            return scooters?.map(User.copyConstructor);
        } catch (e) {
            console.log(e)
        }
    }

    async asyncFindById(id) {
        const url = `${this.resourcesUrl}/${id}`;
        return await this.fetchJson(url);
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

                console.log('Saving user:', user);
                const response = await this.fetchJson(url, options);

                if (response) {
                    console.log('User saved successfully:', response);
                    return response;
                } else {
                    console.error('Failed to save user.');
                    return null;
                }
            }

            const createdUser = User.copyConstructor(user);
            let res;
            res = this.fetchJson(this.resourcesUrl + "/" + user.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(createdUser)
            })
            return User.copyConstructor(res);

        } catch (error) {
            console.error('Error during save:', error);
            return null;
        }
    }

    async login(username, password){
        try {
            console.log('Sending login request...')
            const url=`${this.resourcesUrl}/login`;
            const options={
                method:'POST',
                headers:{
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({username,password})

            };
            const response=await this.fetchJson(url,options);
            if (response){
                console.log('login successful:',response);
                return response
            }else {
                console.error('Login failed: Invalid username or password.');
                return null;
            }
        }catch (error){
            console.error('Error during login:', error);
            return null;
        }
    }
}
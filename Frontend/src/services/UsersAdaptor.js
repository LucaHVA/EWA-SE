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

    async asyncFindById(id) {
        const url = `${this.resourcesUrl}/${id}`;
        return await this.fetchJson(url);
    }

    async save(user, queryParams) {
        try {
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
        } catch (error) {
            console.error('Error during save:', error);
            return null;
        }
    }
}


export class UsersAdaptor{
    resourcesUrl;

    constructor(resourcesUrl) {
        this.resourcesUrl=resourcesUrl;
    }

    async fetchJson(url, options = null){
        try {
            console.log('Fetching data from:', url);
            let response = await fetch(url, options);

            if (response.ok) {
                return await response.json();
            } else {
                console.log('Error during fetch:', response, !response.bodyUsed ? await response.text() : "");
                return null;
            }
        } catch (error) {
            console.error('Error during fetch:', error);
            return null;
        }
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
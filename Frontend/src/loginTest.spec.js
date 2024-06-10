import { shallowMount } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';
import LoginComponent from '@/components/pages/LoginComponent.vue';
import { describe, it, beforeEach, expect, jest } from '@jest/globals';

// Mock object for usersService
const mockUsersService = {
    async login(username, password) {
        if (username === 'validUser' && password === 'validPass') {
            return { ok: true, id: 1, username: "validUser", email: "validUser@example.com" };
        } else {
            return { ok: false };
        }
    },
};

process.env.VUE_APP_API_URL = 'http://localhost:3000';

describe('LoginComponent.vue', () => {
    let wrapper;
    const router = createRouter({
        history: createWebHistory(),
        routes: [{ path: '/', component: LoginComponent }],
    });

    beforeEach(async () => {
        router.push('/');
        await router.isReady();

        wrapper = shallowMount(LoginComponent, {
            global: {
                plugins: [router],
                provide: { usersService: mockUsersService },
            },
        });
    });

    it('renders login form', () => {
        expect(wrapper.find('form').exists()).toBe(true);
        expect(wrapper.find('input#username').exists()).toBe(true);
        expect(wrapper.find('input#password').exists()).toBe(true);
        expect(wrapper.find('button').exists()).toBe(true);
    });

    it('displays an alert message on failed login', async () => {
        wrapper.setData({ username: 'invalidUser', password: 'invalidPass' });
        await wrapper.vm.login();
        expect(wrapper.vm.alert).toBe(true);
        expect(wrapper.find('.alertMessage').exists()).toBe(true);
    });

    it('redirects to home on successful login', async () => {
        jest.spyOn(wrapper.vm.$router, 'push');
        wrapper.setData({ username: 'validUser', password: 'validPass' });
        await wrapper.vm.login();
        expect(wrapper.vm.alert).toBe(false);
        expect(wrapper.vm.$router.push).toHaveBeenCalledWith({ name: 'home' });
    });
});

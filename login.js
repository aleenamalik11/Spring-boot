const loginForm = document.querySelector('form');
      const loginButton = document.getElementById('login-button');

      async function login(event){
        event.preventDefault();
        const userName = loginForm.elements.username.value;
        const password = loginForm.elements.password.value;

        var data = await post(LOGIN, null, {userName, password});

        if (data.status === SUCCESS) {
            console.log('Login successful');
            const token = data.payload.token;
            localStorage.setItem(JWT_TOKEN, token);
            // Do something with the token, like store it in localStorage
            if(data.payload.roleNames.includes(USR_ADMIN))
                window.location.href = ADMIN_PAGE;
            else{
              window.location.href = USR_PAGE + '?id=' + data.payload.userName;
            }
            console.log(token);
          } else {
            console.log('Login failed');
            alert(data.message);
            // Display an error message to the user
          }

      }


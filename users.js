        const token = localStorage.getItem(JWT_TOKEN);
        console.log(token);
		const queryString = window.location.search;
		const urlParams = new URLSearchParams(queryString);
		var userNameArg = urlParams.get('id');
		var fetchedUserData;

		getUserData(userNameArg);

		async function getUserData(userNameArg){
			console.log("userNameArg " + userNameArg);
			const userData = {
                userName: userNameArg,
                };
                console.log(userData);
                try{
                    
                    var responseData = await post(GET_USR, token, userData);

                    if(responseData.status===SUCCESS){
                        // Create a new row for each user and add it to the table
                        console.log(responseData.payload);
                        fetchedUserData=responseData.payload;
                        const tableBody = document.querySelector('#users-table tbody');
                        const row = document.createElement('tr');
                        row.innerHTML = `
                                <td>${responseData.payload.userName}</td>
                                <td>${responseData.payload.name}</td>
                                <td>${responseData.payload.city}</td>
                            `;
                            tableBody.appendChild(row);
    
                    }
                    else{
                        alert(responseData.message);
                    }
                }
                catch(error){
                    console.error(error);
                    alert(ERR_MSG);
                }
                
		}
		
	
		
		function showForm(){
			document.getElementById("userName").value = fetchedUserData.userName;
			document.getElementById("name").value = fetchedUserData.name;
			document.getElementById("city").value = fetchedUserData.city;
		}

		function closeForm(){
			var modal = document.getElementById("myModal");
			modal.style.display = "none";
			location.reload();
		}

		async function updateUser(event){
			event.preventDefault();
			// Get the updated profile data from the form
				console.log("updateee");
				const newUserName = document.getElementById("userName").value;
				var newName = document.getElementById("name").value;
				var newCity = document.getElementById("city").value;

				const newUserData = {
					id: fetchedUserData.id,
					userName: newUserName,
					password: fetchedUserData.password,
					name: newName,
					city: newCity,
					roles: fetchedUserData.roles
				}

				console.log("newUserData " + newUserData);

                try{
                    var responseData = await post(UPDATE_USR, token, newUserData);

                    if( responseData!=null && responseData.payload.userName!=fetchedUserData.userName)	{
                        window.location.href = LOGIN_PAGE;
                    }	
                    else{
                        location.reload();
                    }
                }
                catch(error){
                    console.error(error);
                    alert(ERR_MSG);
                }
		}
			
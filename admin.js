const tableBody = document.querySelector('#users-table tbody');
console.log(tableBody);
var CurrPageNo=0;
const pageSize = 5;

const token = localStorage.getItem(JWT_TOKEN);

displayAllUsers(CurrPageNo, pageSize);
  fetchAllRoles(); 


    console.log(token);

    function createPaginationLinks(numOfPages) {


        const container = document.getElementById('list');
      //console.log(container);
        // Clear any existing pagination links
        container.innerHTML = '';
      console.log("numOfPages " + numOfPages);
        // Generate new pagination links
        for (let i = 0; i < numOfPages; i++) {
          const listItem = document.createElement('li');
          const link = document.createElement('a');
          link.classList.add('page-link');
          link.textContent = i;
          link.id = 'page-' + i;
          link.style = "background-color: lightblue;"
          link.addEventListener('click', function(){              
            setPageNo(this.textContent);
          })
          listItem.classList.add('page-item');
          listItem.appendChild(link);
          container.appendChild(listItem);
          console.log('container' + " " + container);
        }
      }
      
      
      function setPageNo(content){
      
        var table = document.getElementById("users-table");
        var tbody = table.getElementsByTagName("tbody")[0];
        console.log(content);
        var page = content;
        // Loop through all rows and remove them
        var numRows = tbody.rows.length;
        for (var i = 0; i < numRows; i++) {
          tbody.deleteRow(0);
        }
        
        CurrPageNo=page;
        console.log("CurrPageNo" + CurrPageNo);
        displayAllUsers(CurrPageNo, pageSize);
        filterTableRows();
      }
 

      async function displayAllUsers(CurrPageNo, pageSize){
      
        const displayUserData = {
                page: CurrPageNo, 
                size: pageSize
              };
             
          // DISPLAY USER DATA IN TABLE
      
          var userData = await post(GET_USERS, token, displayUserData);
          console.log('userData ' + userData.status);
      
          if(userData.status === ERROR){
              console.log(userData.message);
              alert(userData.message);
          }
          else{
            console.log(userData.payload.content);
            
            //console.log("res " + res);
                if(userData.payload.content){
                  userData.payload.content.forEach(user => {
                      const row = document.createElement('tr');
                      
                      row.innerHTML = `
                          <td >${user.id}</td>
                          <td>${user.name}</td>
                          <td>${user.userName}</td>
                          <td>${user.city}</td>
                      `;
                      tableBody.appendChild(row);
                      
                  });
                }
                
          }
          res= userData.payload.totalPages; 
          createPaginationLinks(res);                  
          
    }
async function fetchAllRoles(){
    var rolesData = await get(GET_ROLES, token);
    console.log('rolesData ' + rolesData);
    if(rolesData.status == SUCCESS){
        return rolesData.payload;
    }
    else{
        console.error(rolesData.message);
    }
}

function filterTableRows(){
    var rows = document.getElementById('users-table').getElementsByTagName('tr');
    var searchQuery = document.getElementById('searchInput').value.toLowerCase();
    console.log(searchQuery);
    
    for (var i = 0; i < rows.length; i++) {
      var username = rows[i].getElementsByTagName('td')[1];
      console.log(username);
      if (username) {
        if (username.textContent.toLowerCase().includes(searchQuery)) {
          rows[i].style.display = '';
        } else {
          rows[i].style.display = 'none';
        }
      }
    }
  } 
  
  async function showAddUserForm(){
   var rolesList = await fetchAllRoles();
      var myModal = document.getElementById("addUserModal");
  
      console.log(rolesList);
      var rolesSelect = document.getElementById("rolesSelect");      
     
      rolesList.forEach(role => {
          console.log(role);
          var option = document.createElement("option");
          option.text = role.split("_")[1];
          rolesSelect.add(option);
      })
  }
  
  function closeAddUserForm(){
    var modal = document.getElementById("addUserModal");
    modal.style.display = "none";
    location.reload();
  }
  
  function closeRemoveUserForm(){
    var modal = document.getElementById("removeUserModal");
    modal.style.display = "none";
    location.reload();
  }
  
  async function addUser(event){
      event.preventDefault(); // Prevent default form submission
      var form = document.getElementById("add-user-form");     
      var fuserName =  document.getElementById("userName").value;
      var fname = document.getElementById("name").value;
      var fpassword=document.getElementById("password").value;
      var fcity = document.getElementById("city").value;
      const select = document.getElementById('rolesSelect');
      var froles = Array.from(select.options).filter(option => option.selected).map(option => ROLE + option.value);           
      
  
      console.log(froles);
  
      const newUserData = {
          name: fname,
          userName: fuserName,
          password: fpassword,
          city: fcity,
          roles: froles
          };
  
          console.log(newUserData);

          var addedUserData = await post(ADD_USR, token, newUserData);
          
          if(addedUserData.status===SUCCESS){
            const newRow = document.createElement('tr');
            newRow.innerHTML = `
                <td>${addedUserData.id}</td>
                <td>${addedUserData.userName}</td>
                <td>${addedUserData.city}</td>
            `;
            tableBody.appendChild(newRow);
                       
        }else{
            console.log(addedUserData.message);
            alert(addedUserData.message);
        }
    
  }
  
  async function removeUser(event){
      event.preventDefault(); 
          var fuserId =  document.getElementById("userId").value;
          const removeUserData = {
              userId: fuserId
          };
  
          console.log(removeUserData);
          console.log(token);
          
          var responseData = await post(REMOVE_USR, token, removeUserData);

          if(responseData.status == SUCCESS){
            location.reload();
          }
          else{
            alert(responseData.message);
          }
  
  }
// import {LocalDateTime, DateTimeFormatter} from '@js-joda/core';
import {DateTimeFormatter, LocalDateTime} from '../node_modules/@js-joda/core/dist/js-joda.esm.js';


const tabLinks = document.getElementsByClassName('tab-links');
const tabContents = document.getElementsByClassName('tab-contents');

const p1 = document.getElementById('p1');
const p2 = document.getElementById('p2');
const p3 = document.getElementById('p3');
const btnClose = document.getElementById('btnClose');
const btnOpen = document.getElementById('btnOpen');

p1.addEventListener('click', function() {
    opentab('skills');
});

p2.addEventListener('click', function() {
    opentab('experience');
});

p3.addEventListener('click', function() {
    opentab('education');
});
btnClose.addEventListener('click', function() {
    closemenu();
});

btnOpen.addEventListener('click', function() {
    openmenu();
});



function opentab(tabname) {
    for (let i = 0; i < tabLinks.length; i++) {
        tabLinks[i].classList.remove('active-link');
    }
    for (let i = 0; i < tabContents.length; i++) {
        tabContents[i].classList.remove('active-tab');
    }
    event.currentTarget.classList.add('active-link');
    document.getElementById(tabname).classList.add('active-tab');
}

// JavaScript code for opening and closing the side menu
const sidemenu = document.getElementById('sidemenu');

function openmenu() {
    sidemenu.style.right = '0';
}

function closemenu() {
    sidemenu.style.right = '-150px';
}

const txtName = document.getElementById('input-name');
const  txtEmail = document.getElementById('input-email');
const txtMessage = document.getElementById('textarea-message');
const btnSubmit = document.getElementById('btnSubmit');



btnSubmit.addEventListener('click', function(eventData) {
    eventData.preventDefault();

    const dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")).toString();
    const name = txtName.value.trim();
    const email = txtEmail.value.trim();
    const textMessage = txtMessage.value.trim();

    const message = {
      dateTime,name,email,textMessage
    };

    const xhr = new XMLHttpRequest();

    xhr.addEventListener('readystatechange', function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 201) {
                resetForm(true);
                txtName.focus();
                showToast('success', 'Saved', 'Message sent successfully');
            } else {
                var errorObj = JSON.parse(xhr.responseText);
                showToast('error', 'Failed to send', errorObj.message);
            }
        }
    });

    xhr.open('POST', 'http://localhost:8080/app/messages', true);

    xhr.setRequestHeader('Content-Type', 'application/json');
    console.log(message);
    xhr.send(JSON.stringify(message));
    console.log(message);
});



function resetForm(clearData) {
    if (clearData) {
        txtName.value = '';
        txtEmail.value = '';
        txtMessage.value = '';
    }
}

function showToast(toastType, header, message) {
    const toast = document.querySelector("#toast .toast");
    toast.classList.remove("text-bg-success", "text-bg-warning", "text-bg-danger");
    switch (toastType) {
        case 'success':
            toast.classList.add('text-bg-success');
            break;
        case 'warning':
            toast.classList.add('text-bg-warning');
            break;
        case 'error':
            toast.classList.add('text-bg-danger');
            break;
        default:
            break;
    }
    document.querySelector("#toast .toast-header > strong").textContent = header;
    document.querySelector("#toast .toast-body").textContent = message;
    toast.classList.add('show');

    setTimeout(function() {
        toast.classList.remove('show');
    }, 5000);
}








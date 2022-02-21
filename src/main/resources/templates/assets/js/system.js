window.fbAsyncInit = function () {
    // FB JavaScript SDK configuration and setup
    FB.init({
        appId: '451491192790768',
        cookie: true,
        xfbml: true,
        version: 'v12.0'
    });

    // Check whether the user already logged in
    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') { }
    });
};

// Load the JavaScript SDK asynchronously
(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));


// Facebook login with JavaScript SDK
function fbLogin() {
    FB.login(function (response) {
    console.log(response);
        if (response.authResponse) {
            var authResponse = response.authResponse;
            FB.api('/me', {
                locale: 'en_US',
                fields: 'id,first_name,last_name,picture'
            },
                function (response) {
                response['authResponse'] = authResponse;
                $.post({
                    url: 'login',
                    data: JSON.stringify(response),
                    headers: {
                        "content-type": "application/json"
                    },
                    success: function (data) {
                        console.log(data, 'data');
                        if (data.status == true) {
                            window.location.href = "/";
                        }
                    },
                    error: function () { }
                    });
                });
        } else {
            // document.getElementById('status').innerHTML = 'User cancelled login or did not fully authorize.';
        }
    }, {
//        scope: 'pages_read_engagement,email,pages_show_list,user_posts'
        scope: 'public_profile,pages_show_list'
    });
}

function fbLogout() {
    FB.logout(function () {
        window.location.href = "logout";
    });
}

function extractDomain(email) {
    const domainRegex = /@(.*)$/;
    const match = email.match(domainRegex);
    return match ? match[1] : null; // Return null if email format is invalid
}

function handleSubmit(event)
{

    try{
        setLoading(true);
        const formData = new FormData(event.target);

        let email;
        let pass;
        for(let pair of formData.entries())
        {
            switch(pair[0])
            {
                case "username":
                    email = pair[1];
                    break;
                case "password":
                    pass = pair[1];
                    break;
            }
        }

        const isEmailValid = emailRegex.test(email);

        if(!isEmailValid)
        {
            setLoading(false);
            return false;
        }

        if(isEmailValid && pass)
            return true;

        const domain = extractDomain(email);

        const matchedDomain = ssoDomains.filter(ssoDomain => ssoDomain.domainName.toLowerCase() === domain.toLowerCase())

        if(matchedDomain.length == 0 || !matchedDomain[0])
        {
            document.getElementById("passField").style.display = "block";
            setLoading(false);
            return false;
        }
        else
        {
            window.location.href = matchedDomain[0].redirectUri;
            return false;
        }

    }
    catch(e)
    {
        setLoading(false);
    }
}

function setLoading(loading)
{
    const loginElement = document.getElementById("submit");

    if(loading)
    {
        loginElement.disabled = true;
        loginElement.children[0].style.visibility = "visible";
        loginElement.children[1].style.visibility = "hidden";
    }
    else
    {
        loginElement.disabled = false;
        loginElement.children[1].style.visibility = "visible";
        loginElement.children[0].style.visibility = "hidden";
    }
}
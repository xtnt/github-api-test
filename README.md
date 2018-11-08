1) First create an extant Github app.
 - https://github.com/organizations/xtnt/settings/apps/new
 
2) Generate a signing key. (download the pem key)
 - https://github.com/organizations/xtnt/settings/apps/extant-dev-app#private-key
 
3) The org must have at least one repo, though you can choose to install the app on one or more. So ensure you have a repo to install the app on. I made this https://github.com/xtnt/github-api-test and created an issue

4) you need the GitHub app id in your code in the application.yml (it gets set as the JWT issuer), You find it in the "About" section of the application definition. here
https://github.com/organizations/xtnt/settings/apps/extant-dev-app

5) Register the app with the "client" In this case our same Xtnt Github account
 - https://github.com/organizations/xtnt/settings/apps/extant-dev-app/installations

6) You also need the installation id of the "client" github organization that you installed the github app. I had to look at the URL of the install page to get this, not sure where else to get?

- https://github.com/organizations/xtnt/settings/installations/440300


# GitLab Command

## Command line instructions
You can also upload existing files from your computer using the instructions below.

## Configure your Git identity
Get started with Git and learn how to configure it.

## Git global setup
Configure your Git identity globally to use it for all current and future projects on your machine:
```
git config --global user.name "TENG PHOUPHANIT"
git config --global user.email "phouphanitteng@gmail.com"
```

## HTTPS
## Create a new repository
```cmd
git clone https://gitlab.com/acleda_gp/traning/student_ms.git
cd student_ms
git switch --create main
touch README.md
git add README.md
git commit -m "add README"
git push --set-upstream origin main
```


Push an existing folder
Go to your folder
```
cd existing_folder
```

Configure the Git repository
```
git init --initial-branch=main
git remote add origin https://gitlab.com/acleda_gp/traning/student_ms.git
git add .
git commit -m "Initial commit"
git push --set-upstream origin main
```

Push an existing Git repository

Go to your repository
```
cd existing_repo
```

Configure the Git repository
```
git remote rename origin old-origin
git remote add origin https://gitlab.com/acleda_gp/traning/student_ms.git
git push --set-upstream origin --all
git push --set-upstream origin --tags
```
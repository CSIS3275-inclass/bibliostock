# About BiblioStock
//TODO

# Help
To clone the repo
```
git clone -b <branch-name> <repository-url>
//e.g. git clone -b dev/models https://github.com/CSIS3275-inclass/bibliostock.git

```

If you're making any change

```
//create your own dev branch 
git branch <name>
git checkout <created branch name> #switch to your new branch
//after you are satisfied with your changes
git add -A //this will all the changes in one commit
git commit -m "<your commit message>"

//alternatively to separate your changes in multiple commits you can run (for each of them)
git add <paths of specific files you want to commit>
git commit -m "<message for that commit>"

//when all changes are staged for commit(s) you can push them to the repo
git push origin <your branch>

//you should now see your new branch in the repo
//later, when your code has been reviewed/discusse you can make a pull request to merge your branch with the main dev branch, then when main dev branch looks good it can be merged to main branch
```

other useful commands
```
git --help //help
git help <command> //help with specific command/
git branch -v //list all the branches in your local repo
git status //list all files with new changes
git diff //see all changes
#or git diff <path> to see changes on a specific file
git restore <path> //to unstage change
git log //see commit history
```

## Resources 

- [Install git](https://www.atlassian.com/git/tutorials/install-git)
- [User Manual](https://git-scm.com/docs/user-manual)
- [References](https://git-scm.com/docs)
- [Collaborating with pull requests](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests)

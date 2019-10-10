from flask import Flask, render_template, url_for, request, redirect, abort, flash
import json, hashlib, io, csv, uuid, requests, os, sys
import urllib.parse

from functools import wraps
from datetime import datetime


app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
lang = "en"



# Establish a connection to the database 
# if we are sing a database, 
#       could also host locally and use the ranger application to call a webapp?





#####################################################################################           Home Page
#   Home Page
##########################################
@app.route('/')
@app.route('/home')
def homepage():
    """Returns the homepage\n
    @TODO: Define content to go on the homepage, currently blank
    """
    return render_template("/{}/subpages/home.html".format(lang))




# Create a set context route
# This will accept POST so that the user can set thir contect in the database
# Params:
# @Param: username    ===>    The username of the person setting context
# @Param: context     ===>    The context that they are searching under

@app.route("/submitcontext", methods=["POST"])
def submitcontext():

    try:
        updated = False

        username = request.form["username"]
        context = request.form["context"]
        headers = {"content-type": "application/json"}

        contextFile = {}
        filename = "context.txt"

        # Open config file and check for an existing context
        with open(filename, "r") as csvfile:
            print("\nopened file\n")
            reader = csv.reader(csvfile, delimiter='=', escapechar='\\', quoting=csv.QUOTE_NONE)
            print("\ncreated a reader\n")
            for row in reader:
                print("\noread first row\n")

                if len(row) != 2:
                    raise csv.Error("Too many fields on row with contents")
                contextFile[row[0]] = row[1]
        
        contextFile[username] = context

        csv.register_dialect('myDialect', delimiter = '=', escapechar='\\', quoting=csv.QUOTE_NONE)
        
        
        # Update / Insert context for the user
        with open(filename, "w") as csvfile:
            fieldnames = ['property', 'value']
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames, dialect="myDialect")            
            for k,v in contextFile.items():
                writer.writerow({"property": k, 'value': v})


        # Flash a message to the user
        if (updated):
            flash([f"Successfully updated user {username}'s context to {context}", "Success"])
        else:
            flash(["Successfully set your context", "Success"])


        return redirect(url_for('homepage'))
    except Exception as e:
        flash(["Was not able to update context", "Error"])
        print(e)
        abort(500)







#####################################################################################           Error Pages
#   Error Handling Functions
##########################################
@app.errorhandler(404)
def notfound(e):
    print(e)
    return render_template("{}/errors/404.html".format(lang))

@app.errorhandler(401)
def unauthorised(e):
    return render_template("{}/errors/401.html".format(lang))
    
@app.errorhandler(500)
def servererror(e):
    return render_template("{}/errors/500.html".format(lang))



### Run the App ###
if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True, port=5000)

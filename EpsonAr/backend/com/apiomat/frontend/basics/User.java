/*
 * Copyright (c) 2011-2013, Apinauten GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this 
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THIS FILE IS GENERATED AUTOMATICALLY. DON'T MODIFY IT.
 */
package com.apiomat.frontend.basics;

import java.util.*;
import com.apiomat.frontend.*;
import com.apiomat.frontend.basics.*;
import com.apiomat.frontend.callbacks.*;
import com.apiomat.frontend.helper.*;

import rpc.json.me.*;


/**
* Generated default class representing a user in your app 
*/
public class User extends AbstractClientDataModel
{
    public static final String apiKey = "318053589588918663";
    public static final String baseURL = "https://apiomat.org/yambas/rest/apps/VirtualGuide";
    public static final String system = "LIVE";
    public static final String sdkVersion = "1.6-64";
    /**
    * Default constructor. Needed for internal processing.
    */
    public User ( )
    {
        super( );
    }

    /**
    * Returns the simple name of this class 
    */
    public String getSimpleName( )
    {
        return "User";
    }

    /**
    * Returns the name of the module where this class belongs to
    */
    public String getModuleName( )
    {
        return "Basics";
    }
    
    /**
    * Returns the system to connect to
    */
    public String getSystem( )
    {

        return system;
    }

    
    /**
    * Initialize Datastore with username/password if not done yet
    *
    * @Exception IllegalStateException Throw if no username/password given
    */
    private void initDatastoreWithMembersCredentialsIfNeeded() 
    {
        try 
        {
            Datastore.getInstance();
        } 
        catch (IllegalStateException e) 
        {
            //if the datastore is not initialized then do so
            if (getUserName() != null && getPassword() != null)
            {
                Datastore.configure(baseURL, apiKey, this.getUserName(), this.getPassword(), sdkVersion, system);
            }
            else
            {
                throw new IllegalStateException("Can't init Datastore without username/password" );
            }
        }
    }
    
    /**
    * Updates this class from server 
    */
    public void loadMe( ) throws ApiomatRequestException
    {
        initDatastoreWithMembersCredentialsIfNeeded();
        load( "models/me" );
    }
    
    /**
     * Updates this class from server in the background and not on the UI thread
     * 
     * @param callback
     */
    public void loadMeAsync(AOMEmptyCallback callback) 
    {
        initDatastoreWithMembersCredentialsIfNeeded();
        loadAsync("models/me", callback);
    }
    
    @Override
    public void save() throws ApiomatRequestException 
    {
        initDatastoreWithMembersCredentialsIfNeeded();
        super.save();
    }
    
    public void saveAsync( final AOMEmptyCallback callback )
    {
        initDatastoreWithMembersCredentialsIfNeeded();
        super.saveAsync(callback);
    }

    /**
    * Requests a new password; user will receive an email to confirm
    */
    public void requestNewPassword( )
    {
        AOMCallback<String> cb = new AOMCallback<String>() {
            @Override
            public void isDone(String refHref, ApiomatRequestException ex) {
            }
        };
        Datastore.getInstance( ).postOnServerAsync(this, "models/requestResetPassword/", cb );
    }

    /**
    * Returns a list of objects of this class filtered by the given query from server
    * @query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
    */
    public static final List<User> getUsers( String query ) throws ApiomatRequestException
    {
        User o = new User();
        return Datastore.getInstance( ).loadFromServer( User.class, o.getModuleName( ),
            o.getSimpleName( ), query );
    }
    
    /**
     * Get a list of objects of this class filtered by the given query from server
     * This method works in the background and call the callback function when finished
     *
     * @param query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
     * @param listAOMCallback The callback method which will called when request is finished
     */
    public static void getUsersAsync(final String query, final AOMCallback<List<User>> listAOMCallback) 
    {
       getUsersAsync(query, false, listAOMCallback);
    }
    
    /**
    * Returns a list of objects of this class filtered by the given query from server
    *
    * @query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
    * @param withReferencedHrefs set to true to get also all HREFs of referenced models
    */
    public static final List<User> getUsers( String query, boolean withReferencedHrefs ) throws Exception
    {
        User o = new User();
        return Datastore.getInstance( ).loadFromServer( User.class, o.getModuleName( ),
            o.getSimpleName( ), withReferencedHrefs, query);
    }
    
    /**
     * Get a list of objects of this class filtered by the given query from server
     * This method works in the background and call the callback function when finished
     *
     * @param query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
     * @param withReferencedHrefs set true to get also all HREFs of referenced models
     * @param listAOMCallback The callback method which will called when request is finished
     */
    public static void getUsersAsync(final String query, final boolean withReferencedHrefs, final AOMCallback<List<User>> listAOMCallback) 
    {
         User o = new  User();
        Datastore.getInstance().loadFromServerAsync(User.class,o.getModuleName(), o.getSimpleName(), withReferencedHrefs, query, listAOMCallback);
    }

    public String getFirstName()
    {
         return this.data.optString( "firstName" );
    }

    public void setFirstName( String arg )
    {
        String firstName = arg;
        this.data.put( "firstName", firstName );
    }
    public String getLastName()
    {
         return this.data.optString( "lastName" );
    }

    public void setLastName( String arg )
    {
        String lastName = arg;
        this.data.put( "lastName", lastName );
    }
    public String getPassword()
    {
         return this.data.optString( "password" );
    }

    public void setPassword( String arg )
    {
        String password = arg;
        this.data.put( "password", password );
    }
    public Map getDynamicAttributes()
    {
        return this.data.optJSONObject( "dynamicAttributes" ).getMyHashMap( );
    }

    public void setDynamicAttributes( Map map )
    {
        if( !this.data.has( "dynamicAttributes" ))
        {
            this.data.put( "dynamicAttributes", new Hashtable( ) );
        }
        else
        {
            this.data.optJSONObject( "dynamicAttributes" ).getMyHashMap( ).clear();
        }
        this.data.optJSONObject( "dynamicAttributes" ).getMyHashMap( ).putAll(map);
    }
    public double getLocLatitude( )
    {
         final JSONArray loc = this.data.optJSONArray( "loc" );
         final Object raw = loc.get( 0 );

         return convertNumberToDouble( raw );
    }
    
    public double getLocLongitude( )
    {
         final JSONArray loc = this.data.optJSONArray( "loc" );
         final Object raw = loc.get( 1 );

         return convertNumberToDouble( raw );
    }
    public void setLocLatitude( double latitude )
    {
        if ( this.data.has( "loc" ) == false )
        {
            this.data.put( "loc", new JSONArray( ) );
        }

        this.data.getJSONArray( "loc" ).put( 0, latitude );
    }
    
    public void setLocLongitude( double longitude )
    {
        if ( this.data.has( "loc" ) == false )
        {
            this.data.put( "loc", new JSONArray( ) );
        }

        this.data.getJSONArray( "loc" ).put( 1, longitude );
    }

    public Date getDateOfBirth( )
    {
        return new Date( this.data.getLong( "dateOfBirth" ) );
    }

    public void setDateOfBirth( Date dateOfBirth )
    {
        this.data.putOpt( "dateOfBirth", dateOfBirth.getTime( ) );
    }

    public String getUserName()
    {
         return this.data.optString( "userName" );
    }

    public void setUserName( String arg )
    {
        String userName = arg;
        this.data.put( "userName", userName );
    }
}

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
package com.apiomat.frontend.virtualguidemain;

import java.util.*;
import com.apiomat.frontend.*;
import com.apiomat.frontend.basics.*;
import com.apiomat.frontend.callbacks.*;
import com.apiomat.frontend.helper.*;

import rpc.json.me.*;


/**
* Generated class for your Person data model 
*/
public class Person extends AbstractClientDataModel
{
    private List<Experience> experiences = new ArrayList<Experience>();
    /**
    * Default constructor. Needed for internal processing.
    */
    public Person ( )
    {
        super( );
    }

    /**
    * Returns the simple name of this class 
    */
    public String getSimpleName( )
    {
        return "Person";
    }

    /**
    * Returns the name of the module where this class belongs to
    */
    public String getModuleName( )
    {
        return "VirtualGuideMain";
    }
    
    /**
    * Returns the system to connect to
    */
    public String getSystem( )
    {

        return User.system;
    }

    /**
    * Returns a list of objects of this class filtered by the given query from server
    * @query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
    */
    public static final List<Person> getPersons( String query ) throws ApiomatRequestException
    {
        Person o = new Person();
        return Datastore.getInstance( ).loadFromServer( Person.class, o.getModuleName( ),
            o.getSimpleName( ), query );
    }
    
    /**
     * Get a list of objects of this class filtered by the given query from server
     * This method works in the background and call the callback function when finished
     *
     * @param query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
     * @param listAOMCallback The callback method which will called when request is finished
     */
    public static void getPersonsAsync(final String query, final AOMCallback<List<Person>> listAOMCallback) 
    {
       getPersonsAsync(query, false, listAOMCallback);
    }
    
    /**
    * Returns a list of objects of this class filtered by the given query from server
    *
    * @query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
    * @param withReferencedHrefs set to true to get also all HREFs of referenced models
    */
    public static final List<Person> getPersons( String query, boolean withReferencedHrefs ) throws Exception
    {
        Person o = new Person();
        return Datastore.getInstance( ).loadFromServer( Person.class, o.getModuleName( ),
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
    public static void getPersonsAsync(final String query, final boolean withReferencedHrefs, final AOMCallback<List<Person>> listAOMCallback) 
    {
         Person o = new  Person();
        Datastore.getInstance().loadFromServerAsync(Person.class,o.getModuleName(), o.getSimpleName(), withReferencedHrefs, query, listAOMCallback);
    }

    public List<Experience> loadExperiences( String query ) throws Exception
    {
        final String refUrl = this.data.optString( "experiencesHref" );
        if( refUrl==null || refUrl.length()==0 )
        {
            return experiences;
        } 
        experiences = Datastore.getInstance( ).loadFromServer( Experience.class, refUrl, query );
        return experiences;
    }
    
    /**
    * Getter for local linked variable
    */
    public List<Experience> getExperiences() 
    {
        return experiences;
    }
    /** 
    * Load referenced object(s) on a background thread and
    * add result from server to member variable of this class.
    * 
    * @param query filter returned references by query    * @param callback callback method which will called after request is finished
    *
    */
    public void loadExperiencesAsync(final String query, final AOMEmptyCallback callback ) {
        final String refUrl = this.data.optString("experiencesHref");
        if (refUrl == null || refUrl.length() == 0) {
            if(callback != null) 
            {
                callback.isDone(new ApiomatRequestException(Status.HREF_NOT_FOUND));
            }
            else
            {
                System.err.println("Error occured: " + Status.HREF_NOT_FOUND.getReasonPhrase());  
            }
            return;
        }
        
        AOMCallback<List<Experience>> cb = new AOMCallback<List<Experience>>() {
            @Override
            public void isDone(List<Experience> result,
                               ApiomatRequestException ex) {
                if (ex == null) {
                    experiences.clear();
                    experiences.addAll(result);
                }
                if(callback != null) 
                {
                    callback.isDone(ex);
                }
                else
                {
                    if(ex != null)
                    {
                        System.err.println("Error occured: " + ex.getMessage());
                    }
                }
            }
        };
        Datastore.getInstance().loadFromServerAsync(Experience.class, refUrl, query, cb);
    }

    public String postExperiences( Experience refData ) throws ApiomatRequestException
    {
        String href = refData.getHref();
        if(href == null || href.length() < 1) 
        {
            throw new ApiomatRequestException(Status.SAVE_REFERENECE_BEFORE_REFERENCING);
        }
        
        String refHref = null;
        /* Let's check if we use offline storage or send req to server */
        if(Datastore.getInstance().sendOffline("POST"))
        {
            refHref = Datastore.getInstance().getOfflineHandler().addTask("POST", getHref(), refData, "experiences" );
        } 
        else
        {
            refHref = Datastore.getInstance( ).postOnServer(refData, this.data.optString("experiencesHref"));
        }
        
        if(refHref!=null && refHref.length()>0)
        {
            //check if local list contains refData with same href
            if(ModelHelper.containsHref(experiences, refHref)==false)
            {
                experiences.add(refData);
            }
        }
        return href;
    }
    
    public void postExperiencesAsync(final Experience refData, final AOMEmptyCallback callback ) {
        String href = refData.getHref();
        if(href == null || href.length() < 1)
        {
            if(callback != null)
            {
                callback.isDone(new ApiomatRequestException(Status.SAVE_REFERENECE_BEFORE_REFERENCING));
            }
            else
            {
                System.err.println("Error occured: " + Status.SAVE_REFERENECE_BEFORE_REFERENCING.getReasonPhrase());
            }
            return;
        }
         /* check if we've use offline storage */
        if(Datastore.getInstance().sendOffline("POST"))
        {
            final String refHref = Datastore.getInstance().getOfflineHandler().addTask("POST", getHref(), refData, "experiences" );
            /* check if local list contains refData with same href */
            if(ModelHelper.containsHref(experiences, refHref)==false)
            {
                experiences.add(refData);
            }
            if(callback != null)
            {
                callback.isDone(null);
            }
        }
        else
        {
            AOMCallback<String> cb = new AOMCallback<String>() {
                @Override
                public void isDone(String refHref, ApiomatRequestException ex) {
                    if(ex == null && refHref!=null && refHref.length()>0)
                    {
                        //check if local list contains refData with same href
                        if(ModelHelper.containsHref(experiences, refHref)==false)
                        {
                            experiences.add(refData);
                        }
                    }
                    if(callback != null)
                    {
                        callback.isDone(ex);
                    }
                    else
                    {
                        System.err.println("Exception was thrown: " + ex.getMessage());
                    }
                }
            };
            Datastore.getInstance( ).postOnServerAsync(refData, this.data.optString("experiencesHref"), cb);
        }
    }
    
    public void removeExperiences( Experience refData ) throws Exception
    {
        final String id = refData.getHref( ).substring( refData.getHref( ).lastIndexOf( "/" ) + 1 );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", getHref(), refData, "experiences" );
        }
        else
        {
            Datastore.getInstance( ).deleteOnServer( this.data.optString("experiencesHref") + "/" + id);
        }
            experiences.remove(refData);
    }
    
    public void removeExperiencesAsync( final Experience refData, final AOMEmptyCallback callback )
    {
        final String id = refData.getHref( ).substring( refData.getHref( ).lastIndexOf( "/" ) + 1 );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", getHref(), refData, "experiences");
            experiences.remove(refData);
            if(callback != null)
            {
                callback.isDone(null);
            }
        }
        else
        {
            AOMEmptyCallback cb = new AOMEmptyCallback() {
                @Override
                public void isDone(ApiomatRequestException ex) {
                    if(ex == null) {
                        experiences.remove(refData);
                    }
                    callback.isDone(ex);
                }
            };
            Datastore.getInstance( ).deleteOnServerAsync( this.data.optString("experiencesHref") + "/" + id, cb);
        }
    }

    public String getName()
    {
         return this.data.optString( "name" );
    }

    public void setName( String arg )
    {
        String name = arg;
        this.data.put( "name", name );
    }
}

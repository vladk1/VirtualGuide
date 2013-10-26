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
* Generated class for your POI data model 
*/
public class POI extends AbstractClientDataModel
{
    /**
    * Default constructor. Needed for internal processing.
    */
    public POI ( )
    {
        super( );
    }

    /**
    * Returns the simple name of this class 
    */
    public String getSimpleName( )
    {
        return "POI";
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
    public static final List<POI> getPOIs( String query ) throws ApiomatRequestException
    {
        POI o = new POI();
        return Datastore.getInstance( ).loadFromServer( POI.class, o.getModuleName( ),
            o.getSimpleName( ), query );
    }
    
    /**
     * Get a list of objects of this class filtered by the given query from server
     * This method works in the background and call the callback function when finished
     *
     * @param query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
     * @param listAOMCallback The callback method which will called when request is finished
     */
    public static void getPOIsAsync(final String query, final AOMCallback<List<POI>> listAOMCallback) 
    {
       getPOIsAsync(query, false, listAOMCallback);
    }
    
    /**
    * Returns a list of objects of this class filtered by the given query from server
    *
    * @query a query filtering the results in SQL style (@see <a href="http://doc.apiomat.com">documentation</a>)
    * @param withReferencedHrefs set to true to get also all HREFs of referenced models
    */
    public static final List<POI> getPOIs( String query, boolean withReferencedHrefs ) throws Exception
    {
        POI o = new POI();
        return Datastore.getInstance( ).loadFromServer( POI.class, o.getModuleName( ),
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
    public static void getPOIsAsync(final String query, final boolean withReferencedHrefs, final AOMCallback<List<POI>> listAOMCallback) 
    {
         POI o = new  POI();
        Datastore.getInstance().loadFromServerAsync(POI.class,o.getModuleName(), o.getSimpleName(), withReferencedHrefs, query, listAOMCallback);
    }

    /**
    * Returns the URL of the resource.
    * @return the URL of the resource
    */
    public String getImageURL()
    {
        if(this.data.isNull( "imageURL" ))
        {
            return null;
        }
        return this.data.optString( "imageURL" ) + ".img?apiKey=" 
            + User.apiKey + "&system=" + this.getSystem();
    }

    public String postImage( byte[] data ) throws Exception
    {
        String href = null;
        if(Datastore.getInstance().sendOffline("POST"))
        {
            final String sendHREF = Datastore.getInstance().createStaticDataHref(true);
            href = Datastore.getInstance().getOfflineHandler().addTask("POST", sendHREF, data);
        }
        else
        {
            href = Datastore.getInstance( ).postStaticDataOnServer( data, true);
        }
        
        if(href != null && href.length() > 0)
        {
            this.data.put( "imageURL", href );
            this.save();
        }
        return href;
    }
    
    public void postImageAsync( final byte[] data, final AOMEmptyCallback _callback )
    {
        AOMCallback<String> cb = new AOMCallback<String>() {
            @Override
            public void isDone(String href, ApiomatRequestException ex) {
                if(ex == null && href!=null && href.length()>0)
                {
                    POI.this.data.put( "imageURL", href );
                    /* save new image reference in model */
                    POI.this.saveAsync(new AOMEmptyCallback() {
                        @Override
                        public void isDone(ApiomatRequestException exception) {
                            if(_callback != null)
                            {
                                _callback.isDone(exception);
                            }
                            else
                            {
                                System.err.println("Exception was thrown: " + exception.getMessage());
                            }
                        }
                    });
                }
                else
                {
                    if(_callback != null && ex != null)
                    {
                        _callback.isDone(ex);
                    }
                    else if(_callback != null && ex == null)
                    {
                        _callback.isDone(new ApiomatRequestException(Status.HREF_NOT_FOUND));
                    }
                    else
                    {
                        System.err.println("Exception was thrown: " + (ex != null?ex.getMessage(): Status.HREF_NOT_FOUND.toString()));
                    }
                }
            }
        };
        
        if(Datastore.getInstance().sendOffline("POST"))
        {
            final String sendHREF = Datastore.getInstance().createStaticDataHref(true);
            String refHref = Datastore.getInstance().getOfflineHandler().addTask("POST", sendHREF, data);
            cb.isDone(refHref, null);
        }
        else
        {
            Datastore.getInstance( ).postStaticDataOnServerAsync( data, true, cb);
        }
    }
    
    public void deleteImage() throws Exception
    {
        final String imageURL = this.data.optString( "imageURL" );
	this.data.remove( "imageURL" );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", imageURL);
            this.save();
        }
        else
        {
            Datastore.getInstance( ).deleteOnServer(imageURL);
            this.save();
        }
    }
    
    public void deleteImageAsync(final AOMEmptyCallback _callback)
    {
        AOMEmptyCallback cb = new AOMEmptyCallback() {
            @Override
            public void isDone(ApiomatRequestException ex)
            {
                if(ex == null )
                {
                    POI.this.data.remove( "imageURL" );
                    /* save deleted image reference in model */
                    POI.this.saveAsync(new AOMEmptyCallback() {
                        @Override
                        public void isDone(ApiomatRequestException exception) {
                            if(_callback != null)
                            {
                                _callback.isDone(exception);
                            }
                            else
                            {
                                System.err.println("Exception was thrown: " + exception.getMessage());
                            }
                        }
                    });
                }
                _callback.isDone(ex);
            }
        };
        final String url = this.data.optString( "imageURL" );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", url);
            cb.isDone(null);
        }
        else
        {
            Datastore.getInstance( ).deleteOnServerAsync( url, cb);
        }
    }

    /**
    * Returns an URL of the image. <br/>
    * You can provide several parameters to manipulate the image:
    * @param width the width of the image, 0 to use the original size. If only width or height are provided, 
    *        the other value is computed.
    * @param height the height of the image, 0 to use the original size. If only width or height are provided, 
    *        the other value is computed.
    * @param backgroundColorAsHex the background color of the image, null or empty uses the original background color. Caution: Don't send the '#' symbol!
    *        Example: <i>ff0000</i>
    * @param alpha the alpha value of the image, null to take the original value.
    * @param format the file format of the image to return, e.g. <i>jpg</i> or <i>png</i>
    * @return the URL of the image
    */
    public String getImageURL(int width, int height, String backgroundColorAsHex, 
        Double alpha, String format)
    {
        String parameters =  ".img?apiKey=" + User.apiKey + "&system=" + this.getSystem();
        parameters += "&width=" + width + "&height=" + height;
        if(backgroundColorAsHex != null) 
        {
            parameters += "&bgcolor=" + backgroundColorAsHex;
        }
        if(alpha != null)
            parameters += "&alpha=" + alpha;
        if(format != null)
            parameters += "&format=" + format;
        return this.data.optString( "imageURL" ) + parameters;
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
    /**
    * Returns the URL of the resource.
    * @return the URL of the resource
    */
    public String getGifURL()
    {
        if(this.data.isNull( "gifURL" ))
        {
            return null;
        }
        return this.data.optString( "gifURL" ) + ".img?apiKey=" 
            + User.apiKey + "&system=" + this.getSystem();
    }

    public String postGif( byte[] data ) throws Exception
    {
        String href = null;
        if(Datastore.getInstance().sendOffline("POST"))
        {
            final String sendHREF = Datastore.getInstance().createStaticDataHref(true);
            href = Datastore.getInstance().getOfflineHandler().addTask("POST", sendHREF, data);
        }
        else
        {
            href = Datastore.getInstance( ).postStaticDataOnServer( data, true);
        }
        
        if(href != null && href.length() > 0)
        {
            this.data.put( "gifURL", href );
            this.save();
        }
        return href;
    }
    
    public void postGifAsync( final byte[] data, final AOMEmptyCallback _callback )
    {
        AOMCallback<String> cb = new AOMCallback<String>() {
            @Override
            public void isDone(String href, ApiomatRequestException ex) {
                if(ex == null && href!=null && href.length()>0)
                {
                    POI.this.data.put( "gifURL", href );
                    /* save new image reference in model */
                    POI.this.saveAsync(new AOMEmptyCallback() {
                        @Override
                        public void isDone(ApiomatRequestException exception) {
                            if(_callback != null)
                            {
                                _callback.isDone(exception);
                            }
                            else
                            {
                                System.err.println("Exception was thrown: " + exception.getMessage());
                            }
                        }
                    });
                }
                else
                {
                    if(_callback != null && ex != null)
                    {
                        _callback.isDone(ex);
                    }
                    else if(_callback != null && ex == null)
                    {
                        _callback.isDone(new ApiomatRequestException(Status.HREF_NOT_FOUND));
                    }
                    else
                    {
                        System.err.println("Exception was thrown: " + (ex != null?ex.getMessage(): Status.HREF_NOT_FOUND.toString()));
                    }
                }
            }
        };
        
        if(Datastore.getInstance().sendOffline("POST"))
        {
            final String sendHREF = Datastore.getInstance().createStaticDataHref(true);
            String refHref = Datastore.getInstance().getOfflineHandler().addTask("POST", sendHREF, data);
            cb.isDone(refHref, null);
        }
        else
        {
            Datastore.getInstance( ).postStaticDataOnServerAsync( data, true, cb);
        }
    }
    
    public void deleteGif() throws Exception
    {
        final String imageURL = this.data.optString( "gifURL" );
	this.data.remove( "gifURL" );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", imageURL);
            this.save();
        }
        else
        {
            Datastore.getInstance( ).deleteOnServer(imageURL);
            this.save();
        }
    }
    
    public void deleteGifAsync(final AOMEmptyCallback _callback)
    {
        AOMEmptyCallback cb = new AOMEmptyCallback() {
            @Override
            public void isDone(ApiomatRequestException ex)
            {
                if(ex == null )
                {
                    POI.this.data.remove( "gifURL" );
                    /* save deleted image reference in model */
                    POI.this.saveAsync(new AOMEmptyCallback() {
                        @Override
                        public void isDone(ApiomatRequestException exception) {
                            if(_callback != null)
                            {
                                _callback.isDone(exception);
                            }
                            else
                            {
                                System.err.println("Exception was thrown: " + exception.getMessage());
                            }
                        }
                    });
                }
                _callback.isDone(ex);
            }
        };
        final String url = this.data.optString( "gifURL" );
        if(Datastore.getInstance().sendOffline("DELETE"))
        {
            Datastore.getInstance().getOfflineHandler().addTask("DELETE", url);
            cb.isDone(null);
        }
        else
        {
            Datastore.getInstance( ).deleteOnServerAsync( url, cb);
        }
    }

    /**
    * Returns an URL of the image. <br/>
    * You can provide several parameters to manipulate the image:
    * @param width the width of the image, 0 to use the original size. If only width or height are provided, 
    *        the other value is computed.
    * @param height the height of the image, 0 to use the original size. If only width or height are provided, 
    *        the other value is computed.
    * @param backgroundColorAsHex the background color of the image, null or empty uses the original background color. Caution: Don't send the '#' symbol!
    *        Example: <i>ff0000</i>
    * @param alpha the alpha value of the image, null to take the original value.
    * @param format the file format of the image to return, e.g. <i>jpg</i> or <i>png</i>
    * @return the URL of the image
    */
    public String getGifURL(int width, int height, String backgroundColorAsHex, 
        Double alpha, String format)
    {
        String parameters =  ".img?apiKey=" + User.apiKey + "&system=" + this.getSystem();
        parameters += "&width=" + width + "&height=" + height;
        if(backgroundColorAsHex != null) 
        {
            parameters += "&bgcolor=" + backgroundColorAsHex;
        }
        if(alpha != null)
            parameters += "&alpha=" + alpha;
        if(format != null)
            parameters += "&format=" + format;
        return this.data.optString( "gifURL" ) + parameters;
    }

    public double getLocationLatitude( )
    {
         final JSONArray loc = this.data.optJSONArray( "location" );
         final Object raw = loc.get( 0 );

         return convertNumberToDouble( raw );
    }
    
    public double getLocationLongitude( )
    {
         final JSONArray loc = this.data.optJSONArray( "location" );
         final Object raw = loc.get( 1 );

         return convertNumberToDouble( raw );
    }
    public void setLocationLatitude( double latitude )
    {
        if ( this.data.has( "location" ) == false )
        {
            this.data.put( "location", new JSONArray( ) );
        }

        this.data.getJSONArray( "location" ).put( 0, latitude );
    }
    
    public void setLocationLongitude( double longitude )
    {
        if ( this.data.has( "location" ) == false )
        {
            this.data.put( "location", new JSONArray( ) );
        }

        this.data.getJSONArray( "location" ).put( 1, longitude );
    }

}

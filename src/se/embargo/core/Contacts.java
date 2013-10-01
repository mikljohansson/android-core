package se.embargo.core;

import java.util.HashMap;
import java.util.Map;

import se.embargo.core.database.Cursors;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contacts {
    private final Map<Object, ContactDetails> _contacts = new HashMap<Object, ContactDetails>();
    private final Context _context;
    private final ContentResolver _resolver;

    private final String[] _contactColumns = new String[] {
    	ContactsContract.Contacts.DISPLAY_NAME,
    	ContactsContract.Contacts.PHOTO_ID};

    private final String[] _phoneLookupColumns = new String[] {
    	ContactsContract.PhoneLookup.DISPLAY_NAME,
    	ContactsContract.PhoneLookup.PHOTO_ID};
    
    private final String[] _photoColumns = new String[] {
    	ContactsContract.CommonDataKinds.Photo.PHOTO};
    
    public static class ContactDetails {
		private static final ContactDetails NOT_FOUND = new ContactDetails(null, null);
		
		public final String name;
		public final Bitmap photo;
		
		public ContactDetails(String name, Bitmap photo) {
			this.name = name;
			this.photo = photo;
		}
	}
	
    /**
     * @param	context	Handle on activity context.
     */
    public Contacts(Context context) {
    	_context = context;
    	_resolver = _context.getContentResolver();
	}

    /**
	 * Fetches contact details based on a phonenumber.
	 * @param	phonenumber	Number to lookup. 
	 * @return				Contact details of null on failure.
	 */
	public ContactDetails getPhonecallDetails(String phonenumber) {
		ContactDetails result = _contacts.get(phonenumber);
		if (result == null) {
		    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
		    Cursor cursor = _resolver.query(uri, _phoneLookupColumns, null, null, null);

		    try {
		        if (cursor != null && cursor.moveToFirst()) {
					String name = Cursors.getString(cursor, ContactsContract.PhoneLookup.DISPLAY_NAME);
					Long photoid = Cursors.getLong(cursor, ContactsContract.PhoneLookup.PHOTO_ID);
					Bitmap photo = getContactThumbnail(photoid);
					
					// Cache the detail under the phonenumber
					result = new ContactDetails(name, photo);
					_contacts.put(phonenumber, result);
		        }
		    }
		    finally {
		        if (cursor != null) {
		            cursor.close();
		        }
		    }
		}
		
		if (result == null) {
			_contacts.put(phonenumber, ContactDetails.NOT_FOUND);
		}
		
		return result != ContactDetails.NOT_FOUND ? result : null;
	}
	
	/**
	 * Fetches contact details.
	 * @param	lookupuri	Permalink lookup uri to find. 
	 * @return				Contact details of null on failure.
	 */
	public ContactDetails getContactDetails(Uri lookupuri) {
		ContactDetails result = _contacts.get(lookupuri);
		if (result == null) {
			Uri uri = ContactsContract.Contacts.lookupContact(_resolver, lookupuri);
			if (uri != null) {
				Cursor cursor = _resolver.query(lookupuri, _contactColumns, null, null, null);
				try {
					if (cursor != null && cursor.moveToFirst()) {
						String name = Cursors.getString(cursor, ContactsContract.Contacts.DISPLAY_NAME);
						Long photoid = Cursors.getLong(cursor, ContactsContract.Contacts.PHOTO_ID);
						Bitmap photo = getContactThumbnail(photoid);

						// Cache the detail under the lookup uri
						result = new ContactDetails(name, photo);
						_contacts.put(lookupuri, result);
					}
				}
				finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
		}
		
		if (result == null) {
			_contacts.put(lookupuri, ContactDetails.NOT_FOUND);
		}
		
		return result != ContactDetails.NOT_FOUND ? result : null;
	}
	
	public Uri getContactVCardUri(Uri lookupuri) {
		Uri contacturi = ContactsContract.Contacts.lookupContact(
			_resolver, lookupuri);
		
		if (contacturi != null) {
			Cursor cursor = _resolver.query(
				contacturi, new String[] {ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
			
			try {
				if (cursor != null && cursor.moveToFirst()) {
					String lookupkey = Cursors.getString(cursor, ContactsContract.Contacts.LOOKUP_KEY);
					return Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupkey);
				}
			}
			finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Fetches a contact photo.
	 * @param	photoid	Id of photo in ContactsContract.Data table.
	 * @return			Photo bitmap or null on failure.
	 */
    private Bitmap getContactThumbnail(long photoid) {
    	if (photoid != 0) {
    		final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photoid);
    	    final Cursor cursor = _resolver.query(uri, _photoColumns, null, null, null);

    	    try {
    	        if (cursor != null && cursor.moveToFirst()) {
    	            final byte[] buffer = cursor.getBlob(0);
    	            if (buffer != null) {
    	                return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    	            }
    	        }
    	    }
    	    finally {
    	    	if (cursor != null) {
    	    		cursor.close();
    	    	}
    	    }
    	}
    	
    	return null;
    }
}

/*
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike
 * 3.0 Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons,
 * 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * The original Urban 3 Dimensional Universe application was created by Sylvain Cambon,
 * AurÃ©lien Chabot, Anthony Foulfoin, JÃ©rÃ´me Dalbert & Johann Legaye.
 * Contact them for other licensing possibilities, using this email address pattern:
 * <first_name> DOT <name> AT etu DOT enseeiht DOT fr .
 * http://www.projet.long.2011.free.fr
 */
package fr.n7.saceca.u3du.model.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * The a byte file reader.<br/>
 * Inspired by <a
 * href="http://www.exampledepot.com/egs/java.io/file2bytearray.html">this code
 * sample</a>.
 */
public class ByteFileReader {

    /** The Constant maxLength. */
    private static final int MAX_FILE_LENGTH = 512 * 1024;


    /**
     * Returns the contents of the file in a byte array.
     * 
     * @param filename
     *            the filename
     * @return the bytes from file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static byte[] getBytesFromFile(String filename) throws IOException {
        File file = null;
        InputStream is = null;
        byte[] bytes = null;

        try {
            file = new File(filename);
            is = new FileInputStream(file);
            long length = file.length();

            // In case the file is too large
            if (length > Integer.MAX_VALUE) {
                throw new IOException("The file " + file.getCanonicalPath()
                        + " is too large to be read.");
            }

            // Create the byte array to hold the data
            bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 1;
            while (offset < bytes.length && numRead >= 0) {
                numRead = is.read(bytes, offset, Math.min(bytes.length - offset, MAX_FILE_LENGTH));
                offset += numRead;
            }
            if (numRead < 0) {
                offset -= numRead; // to correct the last addition
            }

            // Ensure all the bytes have been read
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

        } finally {
            // Close the input stream and return bytes
            if (null != is) {
                is.close();
            }
        }
        return bytes;
    }
}
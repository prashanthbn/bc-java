package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.util.Iterable;
import org.bouncycastle.util.Longs;
import org.bouncycastle.util.Strings;

/**
 * Often a PGP key ring file is made up of a succession of master/sub-key key rings.
 * If you want to read an entire secret key file in one hit this is the class for you.
 */
public class PGPSecretKeyRingCollection
    implements Iterable<PGPSecretKeyRing>
{
    private Map<Long, PGPSecretKeyRing> secretRings = new HashMap<Long, PGPSecretKeyRing>();
    private List<Long> order = new ArrayList<Long>();

    private PGPSecretKeyRingCollection(
        Map<Long, PGPSecretKeyRing> secretRings,
        List<Long> order)
    {
        this.secretRings = secretRings;
        this.order = order;
    }

    public PGPSecretKeyRingCollection(
        byte[] encoding,
        KeyFingerPrintCalculator fingerPrintCalculator)
        throws IOException, PGPException
    {
        this(new ByteArrayInputStream(encoding), fingerPrintCalculator);
    }

    /**
     * Build a PGPSecretKeyRingCollection from the passed in input stream.
     *
     * @param in input stream containing data
     * @throws IOException if a problem parsing the base stream occurs
     * @throws PGPException if an object is encountered which isn't a PGPSecretKeyRing
     */
    public PGPSecretKeyRingCollection(
        InputStream in,
        KeyFingerPrintCalculator fingerPrintCalculator)
        throws IOException, PGPException
    {
        PGPObjectFactory pgpFact = new PGPObjectFactory(in, fingerPrintCalculator);
        Object obj;

        while ((obj = pgpFact.nextObject()) != null)
        {
            // Marker packets must be ignored
            if (obj instanceof PGPMarker)
            {
                continue;
            }
            if (!(obj instanceof PGPSecretKeyRing))
            {
                throw new PGPException(obj.getClass().getName() + " found where PGPSecretKeyRing expected");
            }

            PGPSecretKeyRing pgpSecret = (PGPSecretKeyRing)obj;
            Long key = Longs.valueOf(pgpSecret.getPublicKey().getKeyID());

            secretRings.put(key, pgpSecret);
            order.add(key);
        }
    }

    public PGPSecretKeyRingCollection(
        Collection<PGPSecretKeyRing> collection)
    {
        Iterator<PGPSecretKeyRing> it = collection.iterator();

        while (it.hasNext())
        {
            PGPSecretKeyRing pgpSecret = (PGPSecretKeyRing)it.next();
            Long key = Longs.valueOf(pgpSecret.getPublicKey().getKeyID());

            secretRings.put(key, pgpSecret);
            order.add(key);
        }
    }

    /**
     * Return the number of rings in this collection.
     *
     * @return size of the collection
     */
    public int size()
    {
        return order.size();
    }

    /**
     * return the secret key rings making up this collection.
     */
    public Iterator<PGPSecretKeyRing> getKeyRings()
    {
        return secretRings.values().iterator();
    }

    /**
     * Return an iterator of the key rings associated with the passed in userID.
     *
     * @param userID the user ID to be matched.
     * @return an iterator (possibly empty) of key rings which matched.
     */
    public Iterator<PGPSecretKeyRing> getKeyRings(
        String userID)
    {
        return getKeyRings(userID, false, false);
    }

    /**
     * Return an iterator of the key rings associated with the passed in userID.
     * <p>
     *
     * @param userID       the user ID to be matched.
     * @param matchPartial if true userID need only be a substring of an actual ID string to match.
     * @return an iterator (possibly empty) of key rings which matched.
     */
    public Iterator<PGPSecretKeyRing> getKeyRings(
        String userID,
        boolean matchPartial)
    {
        return getKeyRings(userID, matchPartial, false);
    }

    /**
     * Return an iterator of the key rings associated with the passed in userID.
     * <p>
     *
     * @param userID       the user ID to be matched.
     * @param matchPartial if true userID need only be a substring of an actual ID string to match.
     * @param ignoreCase   if true case is ignored in user ID comparisons.
     * @return an iterator (possibly empty) of key rings which matched.
     */
    public Iterator<PGPSecretKeyRing> getKeyRings(
        String userID,
        boolean matchPartial,
        boolean ignoreCase)
    {
        Iterator<PGPSecretKeyRing> it = this.getKeyRings();
        List<PGPSecretKeyRing> rings = new ArrayList<PGPSecretKeyRing>();

        if (ignoreCase)
        {
            userID = Strings.toLowerCase(userID);
        }

        while (it.hasNext())
        {
            PGPSecretKeyRing secRing = (PGPSecretKeyRing)it.next();
            Iterator<String> uIt = secRing.getSecretKey().getUserIDs();

            while (uIt.hasNext())
            {
                String next = (String)uIt.next();
                if (ignoreCase)
                {
                    next = Strings.toLowerCase(next);
                }

                if (matchPartial)
                {
                    if (next.indexOf(userID) >= 0)
                    {
                        rings.add(secRing);
                    }
                }
                else
                {
                    if (next.equals(userID))
                    {
                        rings.add(secRing);
                    }
                }
            }
        }

        return rings.iterator();
    }

    /**
     * Return the PGP secret key associated with the given key id.
     *
     * @param keyID id of the secret key
     * @return the secret key
     */
    public PGPSecretKey getSecretKey(
        long keyID)
    {
        Iterator<PGPSecretKeyRing> it = this.getKeyRings();

        while (it.hasNext())
        {
            PGPSecretKeyRing secRing = (PGPSecretKeyRing)it.next();
            PGPSecretKey sec = secRing.getSecretKey(keyID);

            if (sec != null)
            {
                return sec;
            }
        }

        return null;
    }

    /**
     * Return the secret key ring which contains the key referred to by keyID.
     *
     * @param keyID id of a secret key
     * @return the secret key ring
     */
    public PGPSecretKeyRing getSecretKeyRing(
        long keyID)
    {
        Long id = Longs.valueOf(keyID);

        if (secretRings.containsKey(id))
        {
            return (PGPSecretKeyRing)secretRings.get(id);
        }

        Iterator<PGPSecretKeyRing> it = this.getKeyRings();

        while (it.hasNext())
        {
            PGPSecretKeyRing secretRing = (PGPSecretKeyRing)it.next();
            PGPSecretKey secret = secretRing.getSecretKey(keyID);

            if (secret != null)
            {
                return secretRing;
            }
        }

        return null;
    }

    /**
     * Return true if a key matching the passed in key ID is present, false otherwise.
     *
     * @param keyID key ID to look for.
     * @return true if keyID present, false otherwise.
     */
    public boolean contains(long keyID)
    {
        return getSecretKey(keyID) != null;
    }

    public byte[] getEncoded()
        throws IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        this.encode(bOut);

        return bOut.toByteArray();
    }

    public void encode(
        OutputStream outStream)
        throws IOException
    {
        BCPGOutputStream out;

        if (outStream instanceof BCPGOutputStream)
        {
            out = (BCPGOutputStream)outStream;
        }
        else
        {
            out = new BCPGOutputStream(outStream);
        }

        Iterator<Long> it = order.iterator();
        while (it.hasNext())
        {
            PGPSecretKeyRing sr = (PGPSecretKeyRing)secretRings.get(it.next());

            sr.encode(out);
        }
    }

    /**
     * Return a new collection object containing the contents of the passed in collection and
     * the passed in secret key ring.
     *
     * @param ringCollection the collection the ring to be added to.
     * @param secretKeyRing  the key ring to be added.
     * @return a new collection merging the current one with the passed in ring.
     * @throws IllegalArgumentException if the keyID for the passed in ring is already present.
     */
    public static PGPSecretKeyRingCollection addSecretKeyRing(
        PGPSecretKeyRingCollection ringCollection,
        PGPSecretKeyRing secretKeyRing)
    {
        Long key = Longs.valueOf(secretKeyRing.getPublicKey().getKeyID());

        if (ringCollection.secretRings.containsKey(key))
        {
            throw new IllegalArgumentException("Collection already contains a key with a keyID for the passed in ring.");
        }

        Map<Long, PGPSecretKeyRing> newSecretRings = new HashMap<Long, PGPSecretKeyRing>(ringCollection.secretRings);
        List<Long> newOrder = new ArrayList<Long>(ringCollection.order);

        newSecretRings.put(key, secretKeyRing);
        newOrder.add(key);

        return new PGPSecretKeyRingCollection(newSecretRings, newOrder);
    }

    /**
     * Return a new collection object containing the contents of this collection with
     * the passed in secret key ring removed.
     *
     * @param ringCollection the collection the ring to be removed from.
     * @param secretKeyRing  the key ring to be removed.
     * @return a new collection merging the current one with the passed in ring.
     * @throws IllegalArgumentException if the keyID for the passed in ring is not present.
     */
    public static PGPSecretKeyRingCollection removeSecretKeyRing(
        PGPSecretKeyRingCollection ringCollection,
        PGPSecretKeyRing secretKeyRing)
    {
        Long key = Longs.valueOf(secretKeyRing.getPublicKey().getKeyID());

        if (!ringCollection.secretRings.containsKey(key))
        {
            throw new IllegalArgumentException("Collection does not contain a key with a keyID for the passed in ring.");
        }

        Map<Long, PGPSecretKeyRing> newSecretRings = new HashMap<Long, PGPSecretKeyRing>(ringCollection.secretRings);
        List<Long> newOrder = new ArrayList<Long>(ringCollection.order);

        newSecretRings.remove(key);

        for (int i = 0; i < newOrder.size(); i++)
        {
            Long r = (Long)newOrder.get(i);

            if (r.longValue() == key.longValue())
            {
                newOrder.remove(i);
                break;
            }
        }

        return new PGPSecretKeyRingCollection(newSecretRings, newOrder);
    }

    /**
     * Support method for Iterable where available.
     */
    public Iterator<PGPSecretKeyRing> iterator()
    {
        return new KeyRingIterator<PGPSecretKeyRing>(order, secretRings);
    }
}

/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Account password hashing.<br>
 * Stored format is "pbkdf2$iterations$salt$hash" (salt and hash in Base64).<br>
 * Legacy unsalted SHA-1 hashes are still verified, so old accounts keep working until they are migrated.
 */
public final class PasswordHash
{
	private static final String PREFIX = "pbkdf2";
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 310000;
	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private PasswordHash()
	{
	}

	/**
	 * @param password
	 * @return the salted hash to store for this password
	 * @throws GeneralSecurityException
	 */
	public static String hash(String password) throws GeneralSecurityException
	{
		final byte[] salt = new byte[SALT_LENGTH];
		SECURE_RANDOM.nextBytes(salt);

		final Base64.Encoder encoder = Base64.getEncoder();
		return PREFIX + "$" + ITERATIONS + "$" + encoder.encodeToString(salt) + "$" + encoder.encodeToString(pbkdf2(password, salt, ITERATIONS, HASH_LENGTH));
	}

	/**
	 * @param password
	 * @param stored the value kept in the database
	 * @return true if the password matches the stored hash
	 * @throws GeneralSecurityException
	 */
	public static boolean verify(String password, String stored) throws GeneralSecurityException
	{
		if ((password == null) || (stored == null))
		{
			return false;
		}

		try
		{
			final Base64.Decoder decoder = Base64.getDecoder();
			final String[] parts = stored.split("\\$");
			if ((parts.length == 4) && parts[0].equals(PREFIX))
			{
				final int iterations = Integer.parseInt(parts[1]);
				final byte[] salt = decoder.decode(parts[2]);
				final byte[] expected = decoder.decode(parts[3]);
				return MessageDigest.isEqual(expected, pbkdf2(password, salt, iterations, expected.length));
			}

			// Legacy unsalted SHA-1
			final byte[] expected = decoder.decode(stored);
			return MessageDigest.isEqual(expected, MessageDigest.getInstance("SHA").digest(password.getBytes(StandardCharsets.UTF_8)));
		}
		catch (IllegalArgumentException e)
		{
			// Malformed stored value
			return false;
		}
	}

	private static byte[] pbkdf2(String password, byte[] salt, int iterations, int length) throws GeneralSecurityException
	{
		final PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, length * 8);
		try
		{
			return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
		}
		finally
		{
			spec.clearPassword();
		}
	}

	/**
	 * Prints the hash of a password, for setting or migrating accounts directly in the database.
	 * @param args the password
	 * @throws GeneralSecurityException
	 */
	public static void main(String[] args) throws GeneralSecurityException
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java -cp L2JLisvus.jar net.sf.l2j.util.PasswordHash <password>");
			return;
		}
		System.out.println(hash(args[0]));
	}
}

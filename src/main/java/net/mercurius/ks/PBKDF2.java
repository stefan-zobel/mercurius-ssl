/*
 * Copyright 2013 Stefan Zobel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mercurius.ks;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class PBKDF2 {

    private static final String PRIV_1 = "@GSr:p\"[dZR6RU;B:s&;4P<3XHPl@\"|r9*Az w#:";
    private static final String PRIV_2 = ",k~m:@HXE-a%%7 c](8J|Yu{d\"`./DK_f'z }^'S";

    // 16384 (= 2^14) iterations
    private static final int ITERS = 1 << 14;

    private static final byte[] SALT_64 = { 59, -76, -110, 53, -98, 81, 100, 67, -90, 113, -119, -32, -5, -61, 44, -9,
            8, -108, 107, -86, 118, -125, -70, -94, 59, -106, -121, -18, 15, 12, 12, -77, 108, 70, 125, 23, -79, 66, 18,
            -51, 67, 55, 53, -28, -35, -92, -54, 37, -101, 57, 100, -128, 41, 24, 107, -25, -106, 73, -108, -110, -34,
            -102, -55, 74 };

    private static final int KEY_LENGTH = 51;

    static char[] getKey(String password) {
        String pwd = password;
        if (pwd == null || "".equals(pwd)) {
            pwd = PRIV_1;
        }
        pwd += PRIV_2;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec spec = new PBEKeySpec(pwd.toCharArray(), SALT_64, ITERS, KEY_LENGTH * 8);
            byte[] encoded = skf.generateSecret(spec).getEncoded();
            char[] ksPwd = Base64.getEncoder().encodeToString(encoded).toCharArray();
            Arrays.fill(encoded, (byte) 0);
            return ksPwd;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private PBKDF2() {
        throw new AssertionError();
    }
}

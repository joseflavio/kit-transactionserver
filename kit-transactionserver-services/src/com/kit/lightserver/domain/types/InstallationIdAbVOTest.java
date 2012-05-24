package com.kit.lightserver.domain.types;

import java.util.Random;

public class InstallationIdAbVOTest {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        Random r = new Random();
        long idA = r.nextLong();
        long idB = r.nextLong();

        InstallationIdAbVO idAb = new InstallationIdAbVO(idA, idB);

        String idAbStr = InstallationIdAbVO.toDBString(idAb);

        System.out.println(idAbStr);


        InstallationIdAbVO parsedIdAb = InstallationIdAbVO.fromDbString("55a7b71d721d02a1:aa177f02e4ce02a2");//aa177f02e4ce02a2

        System.out.println(parsedIdAb);
    }

}

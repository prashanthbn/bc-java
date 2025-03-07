package org.bouncycastle.pqc.crypto.gemss;

import java.security.SecureRandom;

import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.util.Arrays;

class GeMSSEngine
{
    private SecureRandom random;
    final int K; //{128, 192, 256}
    final int HFEn;// {174, 175, 177, 178, 265, 266, 268, 270, 271, 354, 358, 364, 366, 402, 537, 544}
    final int HFEv;// {11, 12, 13, 14, 15, 18, 20, 21, 22, 23, 24, 25, 26, 29, 32, 33, 35}
    final int HFEDELTA;// {10, 12, 13, 15, 18, 21, 22, 24, 25, 29, 30, 32, 33, 34, 35}
    final int NB_ITE;//{1, 3, 4}
    final int HFEDeg;// {17, 129, 513, 640, 1152}
    //Pair of HFEDegI and HFEDegJ:{(9, 0), (7,0), (4,0), (9, 7), (10, 7)}
    final int HFEDegI;// {4, 7, 9, 10}
    final int HFEDegJ;// {7, 0}
    final int HFEnv;//{186, 187, 189, 190, 192, 193, 277, 285, 288, 289, 291, 292, 295, 387, 390, 393, 396, 399, 420, 563, 576}
    final int HFEm;//{162, 163, 243, 253, 256, 257, 324, 333, 384, 512}
    final int NB_BITS_UINT = 64;
    final int HFEnq;
    final int HFEnr;//{9, 10, 12, 14, 15, 18, 24, 25, 32, 38, 44, 46, 47, 49, 50,}
    int HFE_odd_degree;
    int NB_WORD_GFqn;//{3, 5, 6, 7, 9}
    int NB_WORD_GF2nv;
    int NB_MONOMIAL_VINEGAR;
    int NB_MONOMIAL_PK;
    final int HFEnvq;
    final int HFEnvr;//{0, 1, 3, 6, 9, 12, 15, 21, 29, 32, 35, 36, 39, 51, 58, 59, 61, 62}
    int LTRIANGULAR_NV_SIZE;
    final int LTRIANGULAR_N_SIZE;
    final int SIZE_VECTOR_t = 0;
    final int SIZE_SEED_SK;
    int MQnv_GFqn_SIZE;
    final int NB_WORD_MUL;//{6, 9, 12, 13, 17}
    int K3;
    int K2;
    int K1;
    boolean __PENTANOMIAL_GF2N__ = false;
    int NB_WORD_MMUL;//{6, 9, 12, 13, 17}
    int MQv_GFqn_SIZE;
    final int KI;
    final int KI64;
    int K3mod64;
    int K364;
    int K264;
    int K164;
    final boolean ENABLED_REMOVE_ODD_DEGREE;
    final int MATRIXnv_SIZE;
    /* Number of UINT of matrix m*m in GF(2) */
    final int HFEmq;
    final int HFEmr;//{0, 4, 13, 34, 35, 51, 55}
    int NB_WORD_GF2m;
    final int HFEvq;
    final int HFEvr;
    final int NB_WORD_GFqv;
    final int HFEmq8;//{20, 30, 32, 40, 41, 48, 64}
    final int HFEmr8; //{0, 2, 3, 4, 5, 7}
    final int NB_BYTES_GFqm;
    final int ACCESS_last_equations8;
    final int NB_BYTES_EQUATION;
    //final int HFENq8;
    final int HFENr8;
    final int NB_WORD_UNCOMP_EQ;
    final int HFENr8c;
    final int LOST_BITS;
    final int NB_WORD_GF2nvm;
    final int SIZE_SIGN_UNCOMPRESSED;
    final int SIZE_DIGEST;
    final int SIZE_DIGEST_UINT;
    final int HFEnvr8;
    final int MASK8_GF2nv;
    final int NB_BYTES_GFqnv;
    final int VAL_BITS_M;
    final int SIZE_SALT_BITS;
    final int SIZE_SALT_WORD;
    final long MASK_GF2m;
    final int LEN_UNROLLED_64 = 4;
    int NB_COEFS_HFEPOLY;
    int NB_UINT_HFEVPOLY;
    final int MATRIXn_SIZE;
    final long MASK_GF2n;
    final int NB_BYTES_GFqn;
    final int SIZE_SIGN_HFE;
    private int buffer;
    final int SIZE_ROW;
    final int ShakeBitStrength;
    final int Sha3BitStrength;
    final int MLv_GFqn_SIZE;
    int II;
    int POW_II;
    int KP;
    int KX;
    int HFEn_1rightmost;
    Pointer Buffer_NB_WORD_MUL;
    Pointer Buffer_NB_WORD_GFqn;

    public GeMSSEngine(int K, int HFEn, int HFEv, int HFEDELTA, int NB_ITE, int HFEDeg,
                       int HFEDegI, int HFEDegJ)//int HFEs
    {
        this.K = K;
        this.HFEn = HFEn;
        this.HFEv = HFEv;
        this.HFEDELTA = HFEDELTA;
        this.NB_ITE = NB_ITE;
        this.HFEDeg = HFEDeg;
        this.HFEDegI = HFEDegI;
        this.HFEDegJ = HFEDegJ;
        SIZE_ROW = HFEDegI + 1;
        HFEnv = HFEn + HFEv;
        HFEnq = HFEn >>> 6;
        HFEnr = HFEn & 63;
        HFEnvq = HFEnv >>> 6;
        HFEnvr = HFEnv & 63;
        SIZE_SEED_SK = K >>> 3;
        NB_WORD_MUL = ((((HFEn - 1) << 1) >>> 6) + 1);
        KI = HFEn & 63;
        KI64 = 64 - KI;
        HFEm = HFEn - HFEDELTA;
        HFEmq = HFEm >>> 6;
        HFEmr = HFEm & 63;
        HFEvq = HFEv >>> 6;
        HFEvr = HFEv & 63;
        NB_WORD_GFqv = HFEvr != 0 ? HFEvq + 1 : HFEvq;
        HFEmq8 = HFEm >>> 3;
        HFEmr8 = HFEm & 7;
        NB_BYTES_GFqm = HFEmq8 + 1;//(HFEmq8 + ((HFEmr8 != 0) ? 1 : 0));
        NB_WORD_UNCOMP_EQ = ((((HFEnvq * (HFEnvq + 1)) >>> 1) * NB_BITS_UINT) + (HFEnvq + 1) * HFEnvr);
        HFEnvr8 = (HFEnv & 7);
        MASK8_GF2nv = (1 << HFEnvr8) - 1;
        NB_BYTES_GFqnv = ((HFEnv >>> 3) + ((HFEnvr8 != 0) ? 1 : 0));
        VAL_BITS_M = Math.min(HFEDELTA + HFEv, 8 - HFEmr8);
        MASK_GF2m = maskUINT(HFEmr);
        MASK_GF2n = maskUINT(HFEnr);
        NB_BYTES_GFqn = (HFEn >>> 3) + (((HFEn & 7) != 0) ? 1 : 0);
        if (K <= 128)
        {
            ShakeBitStrength = 128;
            Sha3BitStrength = 256;
        }
        else
        {
            ShakeBitStrength = 256;
            if (K <= 192)
            {
                Sha3BitStrength = 384;
            }
            else
            {
                Sha3BitStrength = 512;
            }
        }
        NB_WORD_GFqn = HFEnq + (HFEnr != 0 ? 1 : 0);
        /* To choose macro for NB_WORD_GFqn*64 bits */
        LTRIANGULAR_N_SIZE = (((HFEnq * (HFEnq + 1)) >>> 1) * NB_BITS_UINT + NB_WORD_GFqn * HFEnr);
        MATRIXn_SIZE = HFEn * NB_WORD_GFqn;
        NB_WORD_GF2nv = HFEnvq + (HFEnvr != 0 ? 1 : 0);
        MATRIXnv_SIZE = HFEnv * NB_WORD_GF2nv;
        LTRIANGULAR_NV_SIZE = (((HFEnvq * (HFEnvq + 1)) >>> 1) * NB_BITS_UINT + NB_WORD_GF2nv * HFEnvr);
        NB_MONOMIAL_VINEGAR = (((HFEv * (HFEv + 1)) >>> 1) + 1);
        NB_MONOMIAL_PK = (((HFEnv * (HFEnv + 1)) >>> 1) + 1);
        MQnv_GFqn_SIZE = NB_MONOMIAL_PK * NB_WORD_GFqn;
        MQv_GFqn_SIZE = NB_MONOMIAL_VINEGAR * NB_WORD_GFqn;
        ACCESS_last_equations8 = NB_MONOMIAL_PK * HFEmq8;
        NB_BYTES_EQUATION = (NB_MONOMIAL_PK + 7) >>> 3;
        //HFENq8 = NB_MONOMIAL_PK >>> 3;
        HFENr8 = NB_MONOMIAL_PK & 7;
        HFENr8c = ((8 - HFENr8) & 7);
        //MQ_GFqm8_SIZE = (NB_MONOMIAL_PK * NB_BYTES_GFqm + ((8 - (NB_BYTES_GFqm & 7)) & 7));
        LOST_BITS = (HFEmr8 - 1) * HFENr8c;
        NB_WORD_MMUL = ((((HFEn - 1) << 1) >>> 6) + 1);
        //NB_BITS_MMUL_SUP = NB_WORD_MMUL << 5;
        switch (HFEn)
        {
        case 174:
            //gemss128
            K3 = 13;
            break;
        case 175:
            //bluegemss128, whitegemss128
            K3 = 16;
            break;
        case 177:
            //redgemss128, cyangemss128
            K3 = 8;
            break;
        case 178:
            //magentagemss128
            K3 = 31;
            break;
        case 265:
            //gemss192, bluegemss192
            K3 = 42;
            break;
        case 266:
            //redgemss192,fgemss128,dualmodems128
            K3 = 47;
            break;
        case 268:
            //whitegemss192
            K3 = 25;
            break;
        case 270:
            //cyangemss192
            K3 = 53;
            break;
        case 271:
            //magentagemss192
            K3 = 58;
            break;
        case 354:
            //gemss256
            K3 = 99;
            break;
        case 358:
            //redgemss256, bluegemss256
            K3 = 57;
            break;
        case 364:
            //whitegemss256, cyangemss256
            K3 = 9;
            break;
        case 366:
            //magentagemss256
            K3 = 29;
            break;
        case 402:
            //fgemss192,dualmodems192
            K3 = 171;
            break;
        case 537:
            //fgemss256
            K3 = 10;
            K2 = 2;
            K1 = 1;
            break;
        case 544:
            //dualmodems256
            K3 = 128;
            K2 = 3;
            K1 = 1;
            break;
        default:
            throw new IllegalArgumentException("error: need to add support for HFEn=" + HFEn);
        }
        if (K2 != 0)
        {
            /* Choice of pentanomial for modular reduction in GF(2^n) */
            __PENTANOMIAL_GF2N__ = true;
            K164 = 64 - K1;
            K264 = 64 - K2;
        }
        K3mod64 = K3 & 63;
        K364 = 64 - K3mod64;
        //K364mod64 = K364 & 63;
        int LOG_odd_degree = 0;
        if ((HFEDeg & 1) == 0)//HFEs != 0 ||(HFEDeg & 1) == 0
        {
            // Set to 1 to remove terms which have an odd degree strictly greater than HFE_odd_degree
            ENABLED_REMOVE_ODD_DEGREE = true;
            /* HFE_odd_degree = 1 + 2^LOG_odd_degree */
            LOG_odd_degree = HFEDegI;//(HFEDegI - HFEs);
            HFE_odd_degree = ((1 << (LOG_odd_degree)) + 1);
            if ((HFEDeg & 1) != 0)
            {
                throw new IllegalArgumentException("HFEDeg is odd, so to remove the leading term would decrease the degree.");
            }

            if (HFE_odd_degree > HFEDeg)
            {
                throw new IllegalArgumentException("It is useless to remove 0 term.");
            }
            if (HFE_odd_degree <= 1)
            {
                throw new IllegalArgumentException("The case where the term X^3 is removing is not implemented.");
            }
        }
        else
        {
            ENABLED_REMOVE_ODD_DEGREE = false;
        }
        NB_WORD_GF2m = HFEmq + (HFEmr != 0 ? 1 : 0);
        //NB_BITS_GFqm_SUP = NB_WORD_GF2m << 6;
        NB_WORD_GF2nvm = NB_WORD_GF2nv - NB_WORD_GF2m + (HFEmr != 0 ? 1 : 0);
        SIZE_SIGN_UNCOMPRESSED = NB_WORD_GF2nv + (NB_ITE - 1) * NB_WORD_GF2nvm;
//        if (K <= 80)
//        {
//            SIZE_DIGEST = 20;
//            SIZE_DIGEST_UINT = 3;
//        }
        if (K <= 128)
        {
            SIZE_DIGEST = 32;
            SIZE_DIGEST_UINT = 4;
            //SIZE_2_DIGEST = 64;
            //EQUALHASH_NOCST = ISEQUAL4_NOCST;
            //COPYHASH = COPY4;
        }
        else if (K <= 192)
        {
            SIZE_DIGEST = 48;
            SIZE_DIGEST_UINT = 6;
            //SIZE_2_DIGEST = 96;
//            EQUALHASH_NOCST = ISEQUAL6_NOCST;
//            COPYHASH = COPY6;
        }
        else
        {
            SIZE_DIGEST = 64;
            SIZE_DIGEST_UINT = 8;
            //SIZE_2_DIGEST = 128;
//            EQUALHASH_NOCST = ISEQUAL8_NOCST;
//            COPYHASH = COPY8;
        }
        SIZE_SALT_BITS = 0;
        //SIZE_SALT = 0;
        SIZE_SALT_WORD = 0;
        int NB_COEFS_HFEVPOLY;
        if (((HFEDeg & 1) == 0))//HFEs != 0 || ((HFEDeg & 1) == 0)
        {
            //ENABLED_REMOVE_ODD_DEGREE 0
            NB_COEFS_HFEPOLY = (2 + HFEDegJ + ((HFEDegI * (HFEDegI - 1)) >>> 1) + LOG_odd_degree);
        }
        else
        {
            //ENABLED_REMOVE_ODD_DEGREE 1
            NB_COEFS_HFEPOLY = (2 + HFEDegJ + ((HFEDegI * (HFEDegI + 1)) >>> 1));
        }
        NB_COEFS_HFEVPOLY = NB_COEFS_HFEPOLY + (NB_MONOMIAL_VINEGAR - 1) + (HFEDegI + 1) * HFEv;
        NB_UINT_HFEVPOLY = NB_COEFS_HFEVPOLY * NB_WORD_GFqn;
        SIZE_SIGN_HFE = ((HFEnv + (NB_ITE - 1) * (HFEnv - HFEm) + SIZE_SALT_BITS) + 7) >>> 3;
        MLv_GFqn_SIZE = (HFEv + 1) * NB_WORD_GFqn;
        if (HFEDeg <= 34 || (HFEn > 196 && HFEDeg < 256))
        {
            if (HFEDeg == 17)
            {
                //redgemss128, redgemss192, redgemss256 magentagemss128 magentagemss192 magentagemss256
                II = 4;
            }
            else
            {
                //(HFEDeg == 129)
                //bluegemss192, bluegemss256 cyangemss192 cyangemss256 fgemss128 dualmodems
                II = 6;
            }
            POW_II = 1 << II;
            KP = (HFEDeg >>> II) + ((HFEDeg % POW_II != 0) ? 1 : 0);
            KX = HFEDeg - KP;
        }
        Buffer_NB_WORD_MUL = new Pointer(NB_WORD_MUL);
        Buffer_NB_WORD_GFqn = new Pointer(NB_WORD_GFqn);
        HFEn_1rightmost = 31;
        while (((HFEn - 1) >>> HFEn_1rightmost) == 0)
        {
            --HFEn_1rightmost;
        }
    }

    /**
     * @return 0 if the result is correct, ERROR_ALLOC for error from
     * malloc/calloc functions.
     * @brief Computation of the multivariate representation of a HFEv polynomial.
     * @details Here, for each term of F, X is replaced by sum a_i x_i.
     * @param[in] F   A monic HFEv polynomial in GF(2^n)[X,x_(n+1),...,x_(n+v)]
     * stored with a sparse representation.
     * @param[out] MQS The multivariate representation of F, a MQ system with
     * n equations in GF(2)[x1,...,x_(n+v)]. MQS is stored as one equation in
     * GF(2^n)[x1,...,x_(n+v)] (monomial representation + quadratic form cst||Q).
     * @remark Requires to allocate MQnv_GFqn_SIZE words for MQS.
     * @remark Requirement: F is monic.
     * @remark Constant-time implementation.
     */
    int genSecretMQS_gf2(Pointer MQS, Pointer F)
    {
        Pointer alpha_vec = new Pointer((HFEDegI + 1) * (HFEn - 1) * NB_WORD_GFqn);
        //genCanonicalBasis_gf2n(alpha_vec)
        Pointer a_vec = new Pointer(alpha_vec);
        Pointer F_cp;
        Pointer MQS_cp;
        int i, j;
        a_vec.setOneShiftWithMove(1, NB_BITS_UINT, NB_WORD_GFqn);
        a_vec.moveIncremental();
        for (i = 1; i < HFEnq; ++i)
        {
            a_vec.set(1L);
            a_vec.move(NB_WORD_GFqn);
            /* Put the bit 1 at the position j */
            a_vec.setOneShiftWithMove(1, NB_BITS_UINT, NB_WORD_GFqn);
            a_vec.moveIncremental();
        }
        /* i = NB_WORD_GFqn-1 */
        a_vec.set(1);
        a_vec.move(NB_WORD_GFqn);
        a_vec.setOneShiftWithMove(1, HFEnr, NB_WORD_GFqn);
        a_vec.move(1 - NB_WORD_GFqn);//-NB_WORD_GFqn
        Pointer alpha_vec_tmp = new Pointer(alpha_vec);
        for (i = 0; i < HFEDegI; ++i)
        {
            for (j = 1; j < HFEn; ++j)
            {
                /* a^(2^(i+1)) = (a^(2^i))^2 */
                sqr_gf2n(a_vec, 0, alpha_vec_tmp, 0);
                a_vec.move(NB_WORD_GFqn);
                alpha_vec_tmp.move(NB_WORD_GFqn);
            }
        }
        Pointer lin = new Pointer(HFEn * NB_WORD_GFqn);
        MQS.copyFrom(F, NB_WORD_GFqn);
        F_cp = new Pointer(F, NB_WORD_GFqn);
        /* +NB_WORD_GFqn because the constant is counted 2 times */
        MQS_cp = new Pointer(MQS, MQnv_GFqn_SIZE - MQv_GFqn_SIZE + NB_WORD_GFqn);
        /* Copy the linear and quadratic terms of the constant in GF(2^n)[y1,...,yv] */
        for (i = 1; i < NB_MONOMIAL_VINEGAR; ++i)
        {
            MQS_cp.copyFrom(F_cp, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
            F_cp.move(NB_WORD_GFqn);
        }
        a_vec.changeIndex(alpha_vec);
        //LINEAR_CASE_INIT_REF(a_vec);
        Pointer lin_cp = new Pointer(lin);
        /* j=0 : mul(*F_cp,1) */
        lin_cp.copyFrom(F_cp, NB_WORD_GFqn);
        lin_cp.move(NB_WORD_GFqn);
        for (j = 1; j < HFEn; ++j)
        {
            mul_gf2n(lin_cp, 0, F_cp, 0, a_vec, 0);
            a_vec.move(NB_WORD_GFqn);
            lin_cp.move(NB_WORD_GFqn);
        }
        F_cp.move(NB_WORD_GFqn);
        Pointer a_veci = new Pointer(alpha_vec);
        MQS_cp = new Pointer(MQS, (HFEn + 1) * NB_WORD_GFqn);
        for (j = 0; j < HFEv; ++j)
        {
            MQS_cp.copyFrom(0, F_cp, j * NB_WORD_GFqn, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
        }
        for (i = 1; i < HFEn; ++i)
        {
            MQS_cp.move((HFEn - i) * NB_WORD_GFqn);
            for (j = 0; j < HFEv; ++j)
            {
                mul_gf2n(MQS_cp, 0, F_cp, j * NB_WORD_GFqn, a_veci, 0);
                MQS_cp.move(NB_WORD_GFqn);
            }
            a_veci.move(NB_WORD_GFqn);
        }
        F_cp.move(HFEv * NB_WORD_GFqn);
        LINEAR_CASE_REF(lin, F_cp, a_veci, MQS);
        /* Quadratic term X^3 */
        /* The quadratic terms of MQS are not initialised */
        Pointer a_vecj = new Pointer(alpha_vec);
        //QUADRATIC_CASE_INIT_REF(a_vec, a_vecj);
        /* One term */
        MQS_cp = new Pointer(MQS, NB_WORD_GFqn);
        /* Compute the coefficient of x_0^2 : it is (a^0)^2 = 1 */
        MQS_cp.copyFrom(F_cp, NB_WORD_GFqn);
        //copy_gf2n(MQS, MQS_cp, F, F_cp);
        MQS_cp.move(NB_WORD_GFqn);
        /* Compute the coefficient of x_0*x_(ja+1) : it is 1 for x_0 */
        Pointer tmp1 = new Pointer(NB_WORD_GFqn);
        for (int ja = 0; ja < HFEn - 1; ++ja)
        {
            /* x_0*x_(ja+1) + x_(ja+1)*x_0 */
            tmp1.setRangeFromXor(0, a_vecj, ja * NB_WORD_GFqn, a_vec, ja * NB_WORD_GFqn, NB_WORD_GFqn);
            mul_gf2n(MQS_cp, 0, tmp1, 0, F_cp, 0);
            MQS_cp.move(NB_WORD_GFqn);
        }
        JUMP_VINEGAR_REF(MQS_cp);
        Pointer tmp_i = new Pointer(NB_WORD_GFqn);
        Pointer tmp_j = new Pointer(NB_WORD_GFqn);
        for (int ia = 1; ia < HFEn; ++ia, tmp_i.reset(), tmp_j.reset())
        {
            mul_gf2n(tmp_i, 0, a_vec, 0, F_cp, 0);
            mul_gf2n(tmp_j, 0, a_vecj, 0, F_cp, 0);
            /* Compute the coefficient of x_ia^2 */
            mul_gf2n(MQS_cp, 0, a_vec, 0, tmp_j, 0);
            MQS_cp.move(NB_WORD_GFqn);
            /* Compute the coefficient of x_ia*x_(ja+1) */
            for (int ja = 1; ja < (HFEn - ia); ++ja)
            {
                /* Compute the coefficient of x_ia*x_(ja+1) */
                mul_gf2n(tmp1, 0, tmp_i, 0, a_vecj, ja * NB_WORD_GFqn);
                MQS_cp.copyFrom(tmp1, NB_WORD_GFqn);
                /* Compute the coefficient of x_(ja+1)*x_ia */
                mul_gf2n(tmp1, 0, tmp_j, 0, a_vec, ja * NB_WORD_GFqn);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                //add2_gf2(MQS_cp, tmp1, NB_WORD_GFqn);
                MQS_cp.move(NB_WORD_GFqn);
            }
            JUMP_VINEGAR_REF(MQS_cp);
            a_vec.move(NB_WORD_GFqn);
            a_vecj.move(NB_WORD_GFqn);
        }
        F_cp.move(NB_WORD_GFqn);
        /* Here a_vec = row 2 */
        /* Here a_veci = row 2 */
        /* Linear term X^4 */
        LINEAR_CASE_REF(lin, F_cp, a_veci, MQS);
        /* Other terms, begin at X^5 */
        /* The current term is X^(q^i + q^j) */
        for (i = 2; i < HFEDegI; ++i)
        {
            /* Here a_vec = row i */
            if (ENABLED_REMOVE_ODD_DEGREE)
            {
                j = (((1 << i) + 1) <= HFE_odd_degree) ? 0 : 1;
                a_vecj.changeIndex(j * (HFEn - 1) * NB_WORD_GFqn);
            }
            else
            {
                a_vecj.changeIndex(alpha_vec);
                j = 0;
            }
            for (; j < i; ++j)
            {
                a_veci.changeIndex(a_vec);
                QUADRATIC_CASE_REF(MQS, F_cp, a_veci, a_vecj);
            }
            a_vec.changeIndex(a_veci);
            /* Here a_vec = row i+1 */
            /* j=i */
            LINEAR_CASE_REF(lin, F_cp, a_veci, MQS);
        }
        /* Remainder */
        /* i = HFEDegi */
        /* The current term is X^(q^HFEDegi + q^j) */
        /* Here a_vec = row i */
        if (ENABLED_REMOVE_ODD_DEGREE)
        {
            j = (((1 << i) + 1) <= HFE_odd_degree) ? 0 : 1;
            a_vecj.changeIndex(j * (HFEn - 1) * NB_WORD_GFqn);
        }
        else
        {
            /* Here a_vec = row i */
            a_vecj.changeIndex(alpha_vec);
            j = 0;
        }
        for (; j < HFEDegJ; ++j)//fgemss192 and fgemss256
        {
            a_veci.changeIndex(a_vec);
            QUADRATIC_CASE_REF(MQS, F, a_veci, a_vecj);
        }
        /* Here a_veci = row i+1 */
        a_veci.changeIndex(a_vec);
        //QUADRATIC_MONIC_CASE_REF(a_veci, a_vecj);
        /* One term */
        MQS_cp.changeIndex(NB_WORD_GFqn);
        /* Here a_veci = row i */
        /* Here, a_vecj = row j */
        /* ia = 0 */
        /* Compute the coefficient of x_0^2 : it is (a^0)^2 = 1 */
        MQS_cp.setXor(1);
        MQS_cp.move(NB_WORD_GFqn);
        /* Compute the coefficient of x_0*x_(ja+1) : it is 1 for x_0 */
        for (int ja = 0; ja < HFEn - 1; ++ja)
        {
            /* x_0*x_(ja+1) + x_(ja+1)*x_0 */
            tmp1.setRangeFromXor(0, a_vecj, ja * NB_WORD_GFqn, a_veci, ja * NB_WORD_GFqn, NB_WORD_GFqn);
            MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
        }
        JUMP_VINEGAR_REF(MQS_cp);
        for (int ia = 1; ia < HFEn; ++ia)
        {
            /* Compute the coefficient of x_ia^2 */
            mul_gf2n(tmp1, 0, a_veci, 0, a_vecj, 0);
            MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
            /* Compute the coefficient of x_ia*x_(ja+1) */
            for (int ja = 1; ja < (HFEn - ia); ++ja)
            {
                /* Compute the coefficient of x_ia*x_(ja+1) */
                mul_gf2n(tmp1, 0, a_veci, 0, a_vecj, ja * NB_WORD_GFqn);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                /* Compute the coefficient of x_(ja+1)*x_ia */
                mul_gf2n(tmp1, 0, a_vecj, 0, a_veci, ja * NB_WORD_GFqn);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                MQS_cp.move(NB_WORD_GFqn);
            }
            JUMP_VINEGAR_REF(MQS_cp);
            a_veci.move(NB_WORD_GFqn);
            a_vecj.move(NB_WORD_GFqn);
        }
        /* Here, a_veci = row i+1 */
        /* Here, a_vecj = row j+1 */
        /* Put linear part on "diagonal" of MQS */
        lin_cp = new Pointer(lin);
        MQS_cp = new Pointer(MQS, NB_WORD_GFqn);
        for (i = HFEnv; i > HFEv; --i)
        {
            MQS_cp.setXorRange(0, lin_cp, 0, NB_WORD_GFqn);
            lin_cp.move(NB_WORD_GFqn);
            MQS_cp.move(i * NB_WORD_GFqn);
        }
        return 0;
    }

    int genSecretMQS_gf2_opt(Pointer MQS, Pointer F)
    {
        Pointer a_vec_k;
        Pointer a_vec_kp, buf_k, buf_kp;
        Pointer F_cp;
        Pointer tmp2 = new Pointer(NB_WORD_MUL);
        Pointer tmp3 = new Pointer(NB_WORD_GFqn);
        int i, k, kp;
        /* Vector with linear terms of F */
        Pointer F_lin = new Pointer((HFEDegI + 1) * (HFEv + 1) * NB_WORD_GFqn);
        F_cp = new Pointer(F, MQv_GFqn_SIZE);
        for (i = 0; i <= HFEDegI; ++i)
        {
            for (k = 0; k <= HFEv; ++k)
            {
                F_lin.copyFrom((k * (HFEDegI + 1) + i) * NB_WORD_GFqn, F_cp, 0, NB_WORD_GFqn);
                F_cp.move(NB_WORD_GFqn);
            }
            F_cp.move(i * NB_WORD_GFqn);
        }
        /* Precompute alpha_vec is disabled in the submission */
        Pointer alpha_vec = new Pointer(SIZE_ROW * (HFEn - 1) * NB_WORD_GFqn);
        /* Matrix in GF(2^n) with HFEn-1 rows and (HFEDegI+1) columns */
        /* calloc is useful when it initialises a multiple precision element to 1 */
        genCanonicalBasisVertical_gf2n(alpha_vec);
        /* Constant: copy the first coefficient of F in MQS */
        MQS.copyFrom(F, NB_WORD_GFqn);
        F.move(MQv_GFqn_SIZE);
        MQS.move(NB_WORD_GFqn);
        /* Precompute an other table */
        Pointer buf = new Pointer(HFEDegI * HFEn * NB_WORD_GFqn);
        special_buffer(buf, F, alpha_vec);
        /* k=0 */
        buf_k = new Pointer(buf);
        /* kp=0 */
        buf_kp = new Pointer(buf);
        /* x_0*x_0: quadratic terms of F */
        /* i=0 */
        MQS.copyFrom(buf_kp, NB_WORD_GFqn);
        buf_kp.move(NB_WORD_GFqn);
        for (i = 1; i < HFEDegI; ++i)
        {
            MQS.setXorRange(0, buf_kp, 0, NB_WORD_GFqn);
            buf_kp.move(NB_WORD_GFqn);
        }
        /* At this step, buf_kp corresponds to kp=1 */
        /* x_0: linear terms of F */
        F_cp.changeIndex(F_lin);
        /* X^(2^i) */
        for (i = 0; i <= HFEDegI; ++i)
        {
            /* Next linear term of F: X^(2^i) */
            MQS.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
            F_cp.move(NB_WORD_GFqn);
        }
        MQS.move(NB_WORD_GFqn);
        /* kp=1 (because kp=0 is not stored, it is just (1,1,1,...,1) */
        /* +NB_WORD_GFqn to jump (alpha^kp)^(2^0) */
        a_vec_kp = new Pointer(alpha_vec, NB_WORD_GFqn);
        /* k=0: x_0 x_kp */
        for (kp = 1; kp < HFEn; ++kp)
        {
            /* dot_product(a_vec_kp, buf_k) */
            dotProduct_gf2n(MQS, a_vec_kp, buf_k, HFEDegI);
            a_vec_kp.move(SIZE_ROW * NB_WORD_GFqn);
            /* dot_product(a_vec_k=(1,1,...,1) , buf_kp) */
            for (i = 0; i < HFEDegI; ++i)
            {
                MQS.setXorRange(0, buf_kp, 0, NB_WORD_GFqn);
                buf_kp.move(NB_WORD_GFqn);
            }
            MQS.move(NB_WORD_GFqn);
        }
        /* Vinegar variables */
        for (; kp < HFEnv; ++kp)
        {
            MQS.copyFrom(F_cp, NB_WORD_GFqn);
            F_cp.move(NB_WORD_GFqn);
            for (i = 1; i <= HFEDegI; ++i)
            {
                MQS.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
                F_cp.move(NB_WORD_GFqn);
            }
            MQS.move(NB_WORD_GFqn);
        }
        /* k=0 becomes k=1 */
        /* +NB_WORD_GFqn to jump (alpha^k)^(2^0) */
        a_vec_k = new Pointer(alpha_vec, NB_WORD_GFqn);
        /* Compute the term x_k x_kp */
        for (k = 1; k < HFEn; ++k)
        {
            /* k=0 becomes k=1 */
            buf_k.move(HFEDegI * NB_WORD_GFqn);
            /* kp=k: x_k + x_k*x_k */
            a_vec_kp.changeIndex(a_vec_k);
            buf_kp.changeIndex(buf_k);
            /* Term X^(2^0) of F */
            Buffer_NB_WORD_MUL.mul_gf2x(F_lin, new Pointer(a_vec_kp, -NB_WORD_GFqn));
            /* dot_product(a_vec_k,buf_k) */
            /* i=0 */
            for (i = 1; i <= HFEDegI; ++i)
            {
                /* Next linear term of F: X^(2^i) */
                tmp3.setRangeFromXor(0, buf_kp, 0, F_lin, i * NB_WORD_GFqn, NB_WORD_GFqn);
                tmp2.mul_gf2x(tmp3, a_vec_kp);
                Buffer_NB_WORD_MUL.setXorRange(0, tmp2, 0, NB_WORD_MUL);
                buf_kp.move(NB_WORD_GFqn);
                a_vec_kp.move(NB_WORD_GFqn);
            }
            /* Monic case */
            /* To jump (alpha^kp)^(2^0) */
            a_vec_kp.move(NB_WORD_GFqn);
            rem_gf2n(MQS, 0, Buffer_NB_WORD_MUL);
            MQS.move(NB_WORD_GFqn);
            /* x_k*x_kp */
            for (kp = k + 1; kp < HFEn; ++kp)
            {
                doubleDotProduct_gf2n(MQS, a_vec_kp, buf_k, a_vec_k, buf_kp, HFEDegI);
                a_vec_kp.move(SIZE_ROW * NB_WORD_GFqn);
                buf_kp.move(HFEDegI * NB_WORD_GFqn);
                MQS.move(NB_WORD_GFqn);
            }
            /* Vinegar variables */
            F_cp.changeIndex(F_lin);
            a_vec_k.move(-NB_WORD_GFqn);
            for (; kp < HFEnv; ++kp)
            {
                F_cp.move((HFEDegI + 1) * NB_WORD_GFqn);
                dotProduct_gf2n(MQS, a_vec_k, F_cp, HFEDegI + 1);
                MQS.move(NB_WORD_GFqn);
            }
            a_vec_k.move(NB_WORD_GFqn + SIZE_ROW * NB_WORD_GFqn);
            /* k becomes k+1 */
        }
        /* MQS with v vinegar variables */
        F.move(NB_WORD_GFqn - MQv_GFqn_SIZE);
        MQS.copyFrom(F, NB_WORD_GFqn * (NB_MONOMIAL_VINEGAR - 1));
        MQS.indexReset();
        F.indexReset();
        return 0;
    }

    private void genCanonicalBasisVertical_gf2n(Pointer alpha_vec)
    {
        int i, j;
        /* For each element of the canonical basis */
        for (i = 1; i < HFEn; ++i)
        {
            /* j=0: a^i */
            alpha_vec.set(i >>> 6, 1L << (i & 63));
            /* Compute (a^i)^(2^j) */
            for (j = 0; j < HFEDegI; ++j)
            {
                sqr_gf2n(alpha_vec, NB_WORD_GFqn, alpha_vec, 0);
                alpha_vec.move(NB_WORD_GFqn);
            }
            alpha_vec.move(NB_WORD_GFqn);
        }
        alpha_vec.indexReset();
    }

    private void special_buffer(Pointer buf, Pointer F, Pointer alpha_vec)
    {
        int i, j, k;
        int F_orig = F.getIndex();
        /* Special case: alpha^0 */
        /* F begins to X^3, the first "quadratic" term */
        F.move((NB_WORD_GFqn * (HFEv + 1)) << 1);
        /* X^3 */
        if ((!ENABLED_REMOVE_ODD_DEGREE) || (1 <= HFEDegI))
        {
            buf.copyFrom(F, NB_WORD_GFqn);
            buf.move(NB_WORD_GFqn);
        }
        /* X^5: we jump X^4 because it is linear */
        Pointer F_cp = new Pointer(F, NB_WORD_GFqn * (HFEv + 2));
        /* A_i,j X^(2^i + 2^j) */
        /* min(L,SIZE_ROW-1) */
        for (i = 2; i < SIZE_ROW - 1; ++i)
        {
            /* j=0: A_i,0 */
            buf.copyFrom(F_cp, NB_WORD_GFqn);
            for (j = 1; j < i; ++j)
            {
                F_cp.move(NB_WORD_GFqn);
                buf.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
            }
            buf.move(NB_WORD_GFqn);
            /* To jump a linear term X^(2^i) */
            F_cp.move(NB_WORD_GFqn * (HFEv + 2));
        }
        if (ENABLED_REMOVE_ODD_DEGREE)
        {
            for (; i < (SIZE_ROW - 1); ++i)
            {
                /* j=0 is removed because the term is odd */
                /* j=1: A_i,1 */
                buf.copyFrom(F_cp, NB_WORD_GFqn);
                for (j = 2; j < i; ++j)
                {
                    F_cp.move(NB_WORD_GFqn);
                    buf.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
                }
                buf.move(NB_WORD_GFqn);
                /* To jump a linear term X^(2^i) */
                F_cp.move(NB_WORD_GFqn * (HFEv + 2));
            }
        }
        /* Monic case */
        buf.set1_gf2n(0, NB_WORD_GFqn);
        for (j = 0; j < HFEDegJ; ++j)
        {
            buf.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
            F_cp.move(NB_WORD_GFqn);
        }
        buf.move(NB_WORD_GFqn);
        /* Squares of (alpha^(k+1)) */
        for (k = 0; k < (HFEn - 1); ++k)
        {
            if ((!ENABLED_REMOVE_ODD_DEGREE) || (1 <= HFEDegI))
            {
                /* X^3: i=1,j=0 */
                mul_gf2n(buf, 0, alpha_vec, 0, F, 0);
                buf.move(NB_WORD_GFqn);
            }
            /* X^5: we jump X^4 because it is linear */
            F_cp.changeIndex(F, NB_WORD_GFqn * (HFEv + 2));
            /* A_i,j X^(2^i + 2^j) */
            for (i = 2; i < HFEDegI; ++i)
            {
                dotProduct_gf2n(buf, alpha_vec, F_cp, i);
                buf.move(NB_WORD_GFqn);
                /* To jump quadratic terms + a linear term X^(2^i) */
                F_cp.move((i + HFEv + 1) * NB_WORD_GFqn);
            }
            if (ENABLED_REMOVE_ODD_DEGREE)
            {
                for (; i < (SIZE_ROW - 1); ++i)
                {
                    dotProduct_gf2n(buf, new Pointer(alpha_vec, NB_WORD_GFqn), F_cp, i - 1);
                    buf.move(NB_WORD_GFqn);
                    /* To jump quadratic terms + a linear term X^(2^i) */
                    F_cp.move((i + HFEv) * NB_WORD_GFqn);
                }
            }
            /* j=0: A_i,0 */
            if (HFEDegJ == 0)
            {
                /* Monic case */
                buf.copyFrom(alpha_vec, NB_WORD_GFqn);
                buf.move(NB_WORD_GFqn);
                /* To change the row of alpha_vec */
                alpha_vec.move(SIZE_ROW * NB_WORD_GFqn);
            }
            else
            {
                dotProduct_gf2n(buf, alpha_vec, F_cp, HFEDegJ);
                /* j=HFEDegJ: monic case */
                alpha_vec.move(HFEDegJ * NB_WORD_GFqn);
                buf.setXorRange(0, alpha_vec, 0, NB_WORD_GFqn);
                /* To change the row of alpha_vec */
                alpha_vec.move((SIZE_ROW - HFEDegJ) * NB_WORD_GFqn);
                buf.move(NB_WORD_GFqn);
            }
        }
        buf.indexReset();
        F.changeIndex(F_orig);
        alpha_vec.indexReset();
    }

    private void dotProduct_gf2n(Pointer res, Pointer vec_x, Pointer vec_y, int len)
    {
        Pointer tmp_mul = new Pointer(NB_WORD_MUL);
        int vec_x_orig = vec_x.getIndex();
        int vec_y_orig = vec_y.getIndex();
        int i;
        /* i=0 */
        Buffer_NB_WORD_MUL.mul_gf2x(vec_x, vec_y);
        for (i = 1; i < len; ++i)
        {
            vec_x.move(NB_WORD_GFqn);
            vec_y.move(NB_WORD_GFqn);
            tmp_mul.mul_gf2x(vec_x, vec_y);
            Buffer_NB_WORD_MUL.setXorRange(0, tmp_mul, 0, NB_WORD_MMUL);
        }
        rem_gf2n(res, 0, Buffer_NB_WORD_MUL);
        vec_x.changeIndex(vec_x_orig);
        vec_y.changeIndex(vec_y_orig);
    }

    private void doubleDotProduct_gf2n(Pointer res, Pointer vec_x, Pointer vec_y,
                                       Pointer vec2_x, Pointer vec2_y, int len)
    {
        Pointer acc = new Pointer(NB_WORD_MUL);
        Pointer tmp_mul = new Pointer(NB_WORD_MUL);
        int vec_x_orig = vec_x.getIndex();
        int vec_y_orig = vec_y.getIndex();
        int vec2_x_orig = vec2_x.getIndex();
        int vec2_y_orig = vec2_y.getIndex();
        int i;
        /* i=0 */
        acc.mul_gf2x(vec_x, vec_y);
        for (i = 1; i < len; ++i)
        {
            vec_x.move(NB_WORD_GFqn);
            vec_y.move(NB_WORD_GFqn);
            tmp_mul.mul_gf2x(vec_x, vec_y);
            acc.setXorRange(0, tmp_mul, 0, NB_WORD_MMUL);
        }
        for (i = 0; i < len; ++i)
        {
            tmp_mul.mul_gf2x(vec2_x, vec2_y);
            acc.setXorRange(0, tmp_mul, 0, NB_WORD_MMUL);
            vec2_x.move(NB_WORD_GFqn);
            vec2_y.move(NB_WORD_GFqn);
        }
        rem_gf2n(res, 0, acc);
        vec_x.changeIndex(vec_x_orig);
        vec_y.changeIndex(vec_y_orig);
        vec2_x.changeIndex(vec2_x_orig);
        vec2_y.changeIndex(vec2_y_orig);
    }

    /* Function mul in GF(2^x), then modular reduction */
    void mul_gf2n(Pointer P, int POff, Pointer A, int AOff, Pointer B, int BOff)
    {
        int P_orig = P.getIndex(), A_orig = A.getIndex(), B_orig = B.getIndex();
        A.move(AOff);
        B.move(BOff);
        Buffer_NB_WORD_MUL.reset();
        Buffer_NB_WORD_MUL.mul_gf2x(A, B);
        A.changeIndex(A_orig);
        rem_gf2n(P, POff, Buffer_NB_WORD_MUL);
        B.changeIndex(B_orig);
        P.changeIndex(P_orig);
    }

    private void rem_gf2n(Pointer P, int p_cp, Pointer Pol)
    {
        p_cp += P.getIndex();
        if (K2 != 0)
        {
            if ((HFEn == 544) && (K3 == 128))
            {
                //REM544_PENTANOMIAL_K3_IS_128_GF2X: dualmodems256
                Rem_GF2n.REM544_PENTANOMIAL_K3_IS_128_GF2X(P.array, p_cp, Pol.array, K1,
                    K2, KI, KI64, K164, K264, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
            else if (HFEnr != 0)
            {
                //REM544_PENTANOMIAL_GF2X: fgemss256
                Rem_GF2n.REM544_PENTANOMIAL_GF2X(P.array, p_cp, Pol.array, K1, K2, K3,
                    KI, KI64, K164, K264, K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
        }
        else
        {
            if (HFEn > 256 && HFEn < 289 && K3 > 32 && K3 < 64)
            {
                //REM288_TRINOMIAL_GF2X:   fgemss128 (NOT SURE),
                //REM288_SPECIALIZED_TRINOMIAL_GF2X: whitegemss192, bluegemss192, redgemss192, magentagemss192, cyangemss192
                Rem_GF2n.REM288_SPECIALIZED_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3, KI, KI64,
                    K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
            else if (HFEn == 354)
            {
                //REM384_SPECIALIZED_TRINOMIAL_GF2X: gemss256, whitegemss256, cyangemss256, magentagemss256
                Rem_GF2n.REM384_SPECIALIZED_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3,
                    KI, KI64, K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
            else if (HFEn == 358)
            {
                //REM384_SPECIALIZED358_TRINOMIAL_GF2X: bluegemss256, redgemss256
                Rem_GF2n.REM384_SPECIALIZED358_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3,
                    KI, KI64, K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
            else if (HFEn == 402)
            {
                //REM402_SPECIALIZED_TRINOMIAL_GF2X: fgemss192, dualmodems192
                Rem_GF2n.REM402_SPECIALIZED_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3,
                    KI, KI64, K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
            }
            else
            {
                switch (NB_WORD_MUL)
                {
                case 6:
                    Rem_GF2n.REM192_SPECIALIZED_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3, KI, KI64,
                        K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
                    break;
                case 9:
                    Rem_GF2n.REM288_SPECIALIZED_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3, KI, KI64,
                        K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
                    break;
                case 12:
                    Rem_GF2n.REM384_TRINOMIAL_GF2X(P.array, p_cp, Pol.array, K3,
                        KI, KI64, K364, Buffer_NB_WORD_GFqn.array, MASK_GF2n);
                }
                //REM192_SPECIALIZED_TRINOMIAL_GF2X: gemss128, bluegemss128, redgemss128, whitegemss128, magentagemss128
                //REM288_SPECIALIZED_TRINOMIAL_GF2X: whitegemss192, bluegemss192
            }
        }
    }

    private void LINEAR_CASE_REF(Pointer lin, Pointer F_cp, Pointer a_vec, Pointer MQS)
    {
        Pointer lin_cp = new Pointer(lin);
        /* j=0 : mul(*F_cp,1)=*F_cp */
        lin_cp.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
        lin_cp.move(NB_WORD_GFqn);
        Pointer tmp1 = new Pointer(NB_WORD_GFqn);
        for (int j = 1; j < HFEn; ++j)
        {
            mul_gf2n(tmp1, 0, F_cp, 0, a_vec, 0);
            lin_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
            a_vec.move(NB_WORD_GFqn);
            lin_cp.move(NB_WORD_GFqn);
        }
        F_cp.move(NB_WORD_GFqn);
        a_vec.move(-((HFEn - 1) * NB_WORD_GFqn));
        Pointer MQS_cp = new Pointer(MQS, (HFEn + 1) * NB_WORD_GFqn);
        for (int j = 0; j < HFEv; ++j)
        {
            MQS_cp.setXorRange(0, F_cp, j * NB_WORD_GFqn, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
        }

        for (int ja = 1; ja < HFEn; ++ja)
        {
            MQS_cp.move((HFEn - ja) * NB_WORD_GFqn);
            for (int j = 0; j < HFEv; ++j)
            {
                mul_gf2n(tmp1, 0, F_cp, j * NB_WORD_GFqn, a_vec, 0);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                MQS_cp.move(NB_WORD_GFqn);
            }
            a_vec.move(NB_WORD_GFqn);
        }
        F_cp.move(HFEv * NB_WORD_GFqn);
    }

    private void JUMP_VINEGAR_REF(Pointer MQS_cp)
    {
        MQS_cp.move(HFEv * NB_WORD_GFqn);
    }

    /* Compute (*F_cp)*a_vec[ia]*a_vec[ja] */
    /* a_vec[ia]*a_vec[ja] is the term x_ia * x_(ja+1) */
    private void QUADRATIC_CASE_REF(Pointer MQS, Pointer F_cp, Pointer a_veci, Pointer a_vecj)
    {
        Pointer MQS_cp = new Pointer(MQS, NB_WORD_GFqn);
        Pointer tmp1 = new Pointer(NB_WORD_GFqn);
        Pointer tmp_i = new Pointer(NB_WORD_GFqn);
        Pointer tmp_j = new Pointer(NB_WORD_GFqn);
        /* Here a_veci = row i */
        /* Here, a_vecj = row j */
        /* ia = 0 */
        /* Compute the coefficient of x_0^2 : it is (a^0)^2 = 1 */
        MQS_cp.setXorRange(0, F_cp, 0, NB_WORD_GFqn);
        MQS_cp.move(NB_WORD_GFqn);
        /* Compute the coefficient of x_0*x_(ja+1) : it is 1 for x_0 */
        for (int ja = 0; ja < HFEn - 1; ++ja)
        {
            /* x_0*x_(ja+1) + x_(ja+1)*x_0 */
            tmp1.setRangeFromXor(0, a_vecj, ja * NB_WORD_GFqn, a_veci, ja * NB_WORD_GFqn, NB_WORD_GFqn);
            mul_gf2n(tmp_i, 0, tmp1, 0, F_cp, 0);
            MQS_cp.setXorRange(0, tmp_i, 0, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
        }
        JUMP_VINEGAR_REF(MQS_cp);
        for (int ia = 1; ia < HFEn; ++ia)
        {
            mul_gf2n(tmp_i, 0, a_veci, 0, F_cp, 0);
            mul_gf2n(tmp_j, 0, a_vecj, 0, F_cp, 0);
            /* Compute the coefficient of x_ia^2 */
            mul_gf2n(tmp1, 0, a_veci, 0, tmp_j, 0);
            MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
            MQS_cp.move(NB_WORD_GFqn);
            /* Compute the coefficient of x_ia*x_(ja+1) */
            for (int ja = 1; ja < (HFEn - ia); ++ja)
            {
                /* Compute the coefficient of x_ia*x_(ja+1) */
                mul_gf2n(tmp1, 0, tmp_i, 0, a_vecj, ja * NB_WORD_GFqn);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                /* Compute the coefficient of x_(ja+1)*x_ia */
                mul_gf2n(tmp1, 0, tmp_j, 0, a_veci, ja * NB_WORD_GFqn);
                MQS_cp.setXorRange(0, tmp1, 0, NB_WORD_GFqn);
                MQS_cp.move(NB_WORD_GFqn);
            }
            JUMP_VINEGAR_REF(MQS_cp);
            a_veci.move(NB_WORD_GFqn);
            a_vecj.move(NB_WORD_GFqn);
        }
        /* Here, a_veci = row i+1 */
        F_cp.move(NB_WORD_GFqn);
    }

    /**
     * @brief Reduction in GF(2^n) of a (2n-1)-coefficients square in GF(2)[x].
     * @details The odd degree terms are assumed to be null, and so are not
     * considered.
     * @param[in] Pol A (2n-1)-coefficients square in GF(2)[x].
     * @param[out] P   P is Pol reduced in GF(2^n).
     * @remark Requirement: the odd degree terms of Pol are null.
     * @remark Requirement: the n-degree irreducible polynomial defining GF(2^n)
     * must be a trinomial or a pentanomial.
     * @remark Constant-time implementation.
     */
    private void remsqr_gf2n_ref(Pointer C, Pointer A)
    {
        //sqr_nocst_gf2x
        //sqr_no_simd_gf2x_ref2
        int i;
        long[] res = new long[NB_WORD_MUL];
        for (i = 0; i < NB_WORD_MUL; ++i)
        {
            res[i] = A.get(i);
        }
        int loop_end;
        /* Only the even degree terms are not zero */
        if (((HFEn - 2 + K3) & 1) != 0)
        {
            loop_end = HFEn - 1 + K3;
        }
        else
        {
            //dualmodems256
            loop_end = HFEn - 2 + K3;
        }
        long bit_i;
        int ind;
        for (i = (HFEn - 1) << 1; i >= loop_end; i -= 2)
        {
            /* Extraction of bit_i x^i */
            bit_i = (res[i >>> 6] >>> (i & 63)) & 1L;
            /* x^n = 1 + ... */
            ind = i - HFEn;
            res[ind >>> 6] ^= bit_i << (ind & 63);
            if (__PENTANOMIAL_GF2N__)
            {
                /* ... + x^;K1 + ... */
                ind = i - HFEn + K1;
                res[ind >>> 6] ^= bit_i << (ind & 63);
                /* ... + x^;K2 + ... */
                ind = i - HFEn + K2;
                res[ind >>> 6] ^= bit_i << (ind & 63);
            }
            /* ... + x^K3 */
            ind = i - HFEn + K3;
            res[ind >>> 6] ^= bit_i << (ind & 63);
        }
        for (++i; i >= HFEn; --i)
        {
            /* Extraction of bit_i x^i */
            bit_i = (res[i >>> 6] >>> (i & 63)) & 1L;
            /* x^n = 1 + ... */
            ind = i - HFEn;
            res[ind >>> 6] ^= bit_i << (ind & 63);
            if (__PENTANOMIAL_GF2N__)
            {
                /* ... + x^;K1 + ... */
                ind = i - HFEn + K1;
                res[ind >>> 6] ^= bit_i << (ind & 63);
                /* ... + x^;K2 + ... */
                ind = i - HFEn + K2;
                res[ind >>> 6] ^= bit_i << (ind & 63);
            }
            /* ... + x^K3 */
            ind = i - HFEn + K3;
            res[ind >>> 6] ^= bit_i << (ind & 63);
        }
        for (i = 0; i < NB_WORD_GFqn; ++i)
        {
            C.set(i, res[i]);
        }
        C.setAnd(NB_WORD_GFqn - 1, MASK_GF2n);
    }

    /* Function sqr in GF(2^x), then modular reduction */
    private void sqr_gf2n(Pointer C, int c_shift, Pointer A, int a_shift)
    {
        a_shift += A.cp;
        Buffer_NB_WORD_MUL.reset();
        switch (NB_WORD_MUL)
        {
        case 6:
            Sqr_GF2n.SQR192_NO_SIMD_GF2X(Buffer_NB_WORD_MUL.array, A.array, a_shift);
            break;
        case 9:
            Sqr_GF2n.SQR288_NO_SIMD_GF2X(Buffer_NB_WORD_MUL.array, A.array, a_shift);
            break;
        case 12:
            Sqr_GF2n.SQR384_NO_SIMD_GF2X(Buffer_NB_WORD_MUL.array, A.array, a_shift);
            break;
        case 13:
            Sqr_GF2n.SQR416_NO_SIMD_GF2X(Buffer_NB_WORD_MUL.array, A.array, a_shift);
            break;
        case 17:
            Sqr_GF2n.SQR544_NO_SIMD_GF2X(Buffer_NB_WORD_MUL.array, A.array, a_shift);
            break;
        default:
//            Buffer_NB_WORD_MUL.reset();
//            Buffer_NB_WORD_MUL.sqr_nocst_gf2x(A, NB_WORD_GFqn, NB_WORD_MUL);
//            break;
        }
        rem_gf2n(C, c_shift, Buffer_NB_WORD_MUL);
    }

    private long maskUINT(int k)
    {
        return k != 0 ? (1L << k) - 1L : -1L;
    }

    /* cleanLowerMatrixn: HFEnq, HFEnr
     * cleanLowerMatrixnv: HFEnvq, HFEnvr
     * */
    void cleanLowerMatrix(Pointer L, FunctionParams cleanLowerMatrix)
    {
        int nq, nr;
        long mask;
        int iq, ir;
        //int LTRIANGULAR_SIZE;
        switch (cleanLowerMatrix)
        {
        case N:
            nq = HFEnq;
            nr = HFEnr;
            //LTRIANGULAR_SIZE = LTRIANGULAR_N_SIZE;
            break;
        case NV:
            nq = HFEnvq;
            nr = HFEnvr;
            //LTRIANGULAR_SIZE = LTRIANGULAR_NV_SIZE;
            break;
        default:
            throw new IllegalArgumentException("");
        }
        Pointer L_cp = new Pointer(L);
        //randombytes function is used by GENLOWMATRIX_GF2 function which needs the following line:
//        L_cp.fillRandom(random, LTRIANGULAR_SIZE << 3);
        //randombytes((unsigned char*)L,LTRIANGULAR_SIZE<<3);
        /* for each row */
        for (iq = 1; iq <= nq; ++iq)
        {
            mask = 0;
            for (ir = 0; ir < NB_BITS_UINT; ++ir)
            {
                /* Put the bit of diagonal to 1 + zeros after the diagonal */
                L_cp.setAnd(mask);
                L_cp.setXor(1L << ir);
                mask <<= 1;
                ++mask;
                L_cp.move(iq);
            }
            /* Next column */
            L_cp.moveIncremental();
        }
        /* iq = HFEnq */
        mask = 0;
        for (ir = 0; ir < nr; ++ir)
        {
            /* Put the bit of diagonal to 1 + zeros after the diagonal */
            L_cp.setAnd(mask);
            L_cp.setXor(1L << ir);
            mask <<= 1;
            ++mask;
            L_cp.move(iq);
        }
    }

    /**
     * @brief Compute the inverse of S=LU a matrix (n,n) or (n+v, n+v) in GF(2), in-place.
     * @details Gauss-Jordan: transform S to Identity and Identity to S^(-1).
     * Here, we do not need to transform S to Identity.
     * We use L to transform Identity to a lower triangular S',
     * then we use U to transform S' to S^(-1).
     * @param[in,out] S   S_inv=L*U, an invertible matrix (n,n) in GF(2),
     * its inverse will be computed in-place.
     * @param[in] L_orig   A lower triangular matrix (n,n) in GF(2).
     * @param[in] U_orig   An upper triangular matrix (n,n) in GF(2), but we
     * require to store its transpose (i.e. contiguous following the columns).
     * @param[in] imluParams chooses size of matrix (n,n) or (n+v, n+v)
     * @remark Requirement: S is invertible.
     * @remark Constant-time implementation.
     */
    void invMatrixLU_gf2(Pointer S, Pointer L_orig, Pointer U_orig, FunctionParams imluParams)
    {
        Pointer Sinv_cpi, Sinv_cpj;
        Pointer L_cpj = new Pointer(L_orig);
        Pointer L = new Pointer(L_orig);
        Pointer U = new Pointer(U_orig);
        int i, iq, ir, j;
        int outloopbound, innerloopbound, nextrow, ifCondition, endOfU;
        switch (imluParams)
        {
        case NV:
            S.setRangeClear(0, MATRIXnv_SIZE);
            outloopbound = HFEnvq;
            innerloopbound = HFEnv - 1;
            nextrow = NB_WORD_GF2nv;
            ifCondition = HFEnvr;
            endOfU = LTRIANGULAR_NV_SIZE;
            break;
        case N:
            S.setRangeClear(0, MATRIXn_SIZE);
            outloopbound = HFEnq;
            innerloopbound = HFEn - 1;
            nextrow = NB_WORD_GFqn;
            ifCondition = HFEnr;
            endOfU = LTRIANGULAR_N_SIZE;
            break;
        default:
            throw new IllegalArgumentException("Invalid Input");
        }
        /* Initialize to 0 */
        Sinv_cpi = new Pointer(S);
        Sinv_cpj = new Pointer(S);
        /* for each row of S and of S_inv, excepted the last block */
        for (i = 0, iq = 0; iq < outloopbound; ++iq)
        {
            for (ir = 0; ir < NB_BITS_UINT; ++ir, ++i)
            {
                /* The element of the diagonal is 1 */
                Sinv_cpi.setXor(iq, 1L << ir);
                Sinv_cpj.changeIndex(Sinv_cpi);
                L_cpj.changeIndex(L);
                /* for the next rows */
                for (j = i; j < innerloopbound; ++j)
                {
                    /* next row */
                    Sinv_cpj.move(nextrow);
                    L_cpj.move((j >>> 6) + 1);
                    Sinv_cpj.setXorRangeAndMask(0, Sinv_cpi, 0, iq + 1, -((L_cpj.get() >>> ir) & 1L));
                }
                /* Next row */
                Sinv_cpi.move(nextrow);
                L.move(iq + 1);
            }
            /* Next column */
            L.moveIncremental();
        }
        if (ifCondition > 1)
        {
            for (ir = 0; ir < (ifCondition - 1); ++ir, ++i)
            {
                /* The element of the diagonal is 1 */
                Sinv_cpi.setXor(iq, 1L << ir);
                Sinv_cpj.changeIndex(Sinv_cpi);
                L_cpj.changeIndex(L);
                /* for the next rows */
                for (j = i; j < innerloopbound; ++j)
                {
                    /* next row */
                    Sinv_cpj.move(nextrow);
                    L_cpj.move((j >>> 6) + 1);
                    Sinv_cpj.setXorRangeAndMask(0, Sinv_cpi, 0, iq + 1, -((L_cpj.get() >>> ir) & 1L));
                }
                /* Next row */
                Sinv_cpi.move(nextrow);
                L.move(iq + 1);
            }
            /* ir = HFEnvr-1 */
            Sinv_cpi.setXor(iq, 1L << ir);
            Sinv_cpi.move(nextrow);
        }
        else if (ifCondition == 1)
        {
            /* ir = 0 */
            Sinv_cpi.set(iq, 1);
            Sinv_cpi.move(nextrow);
        }
        /* Here, Sinv_cpi is at the end of S_inv */
        /* End of U */
        U.move(endOfU);
        /* for each row excepted the first */
        for (i = innerloopbound; i > 0; --i)
        {
            /* Previous row */
            U.move(-1 - (i >>> 6));
            /* Row i of Sinv */
            Sinv_cpi.move(-nextrow);
            /* Row j of Sinv */
            Sinv_cpj.changeIndex(S);
            /* for the previous rows */
            for (j = 0; j < i; ++j)
            {
                /* pivot */
                Sinv_cpj.setXorRangeAndMask(0, Sinv_cpi, 0, nextrow, -(((U.get(j >>> 6)) >>> (j & 63)) & 1L));
                /* next row */
                Sinv_cpj.move(nextrow);
            }
        }
    }

    enum FunctionParams
    {
        NV, NVN, V, N, M, NVN_Start
    }

    void vecMatProduct(Pointer res, Pointer vec, Pointer S_orig, int start, FunctionParams vecMatProduct)
    {
        int gf2_len, S_cp_increase, loopir_param, nq;
        long bit_ir;
        int iq = 0, ir = 0;
        Pointer S = new Pointer(S_orig);
        switch (vecMatProduct)
        {
        case NV:
            //VECMATPROD(PREFIX_NAME(vecMatProductnv_64),set0_gf2nv,LOOPIR_NV,REM_NV,HFEnvq)
            res.setRangeClear(0, NB_WORD_GF2nv);
            nq = HFEnvq;
            gf2_len = NB_WORD_GF2nv;
            S_cp_increase = NB_WORD_GF2nv;
            break;
        case NVN:
            //VECMATPROD(PREFIX_NAME(vecMatProductnvn_64),set0_gf2n,LOOPIR_N,REM_NV,HFEnvq)
            res.setRangeClear(0, NB_WORD_GFqn);
            gf2_len = NB_WORD_GFqn;
            S_cp_increase = NB_WORD_GFqn;
            nq = HFEnvq;
            break;
        case V:
            //VECMATPROD(PREFIX_NAME(vecMatProductv_64),set0_gf2n,LOOPIR_N,REM_V,HFEvq)
            res.setRangeClear(0, NB_WORD_GFqn);
            gf2_len = NB_WORD_GFqn;
            S_cp_increase = NB_WORD_GFqn;
            nq = HFEvq;
            break;
        case N:
            //VECMATPROD(PREFIX_NAME(vecMatProductn_64),set0_gf2n,LOOPIR_N,REM_N,HFEnq)
            res.setRangeClear(0, NB_WORD_GFqn);
            gf2_len = NB_WORD_GFqn;
            S_cp_increase = NB_WORD_GFqn;
            nq = HFEnq;
            break;
        case M:
            //VECMATPROD(PREFIX_NAME(vecMatProductm_64),set0_gf2m,LOOPIR_M,REM_M,HFEnq)
            res.setRangeClear(0, NB_WORD_GF2m);
            nq = HFEnq;
            gf2_len = NB_WORD_GF2m;
            S_cp_increase = NB_WORD_GFqn;
            break;
        case NVN_Start:
            //VECMATPROD_START(PREFIX_NAME(vecMatProductnvn_start_64),set0_gf2n, LOOPIR_START_N,REM_START_NV,HFEnvq)
            res.setRangeClear(0, NB_WORD_GFqn);
            nq = HFEnvq;
            gf2_len = NB_WORD_GFqn;
            S_cp_increase = NB_WORD_GFqn;
            ir = start & 63;
            iq = start >>> 6;
            break;
        default:
            throw new IllegalArgumentException("Invalid input for vecMatProduct");
        }
        /* for each bit of vec excepted the last block */
        for (; iq < nq; ++iq)
        {
            if (vecMatProduct != FunctionParams.NVN_Start)
            {
                bit_ir = vec.get(iq);
            }
            else
            {
                bit_ir = vec.get(iq) >>> ir;
            }
            for (; ir < 64; ++ir)
            {
                res.setXorRangeAndMask(0, S, 0, gf2_len, -(bit_ir & 1L));
                /* next row of S */
                S.move(S_cp_increase);
                bit_ir >>>= 1;
            }
            ir = 0;
        }
        /* the last block */
        switch (vecMatProduct)
        {
        case NV:
            //VECMATPROD(PREFIX_NAME(vecMatProductnv_64),set0_gf2nv,LOOPIR_NV,REM_NV,HFEnvq)
        case NVN:
            //VECMATPROD(PREFIX_NAME(vecMatProductnvn_64),set0_gf2n,LOOPIR_N,REM_NV,HFEnvq)
            if (HFEnvr == 0)
            {
                return;
            }
            bit_ir = vec.get(HFEnvq);
            loopir_param = HFEnvr;
            break;
        case V:
            //VECMATPROD(PREFIX_NAME(vecMatProductv_64),set0_gf2n,LOOPIR_N,REM_V,HFEvq)
            if (HFEvr == 0)
            {
                return;
            }
            bit_ir = vec.get(HFEvq);
            loopir_param = HFEvr;
            break;
        case N:
        case M:
            //VECMATPROD(PREFIX_NAME(vecMatProductm_64),set0_gf2m,LOOPIR_M,REM_M,HFEnq)
            //VECMATPROD(PREFIX_NAME(vecMatProductn_64),set0_gf2n,LOOPIR_N,REM_N,HFEnq)
            bit_ir = vec.get(HFEnq);
            loopir_param = HFEnr;
            break;
        case NVN_Start:
            //VECMATPROD_START(PREFIX_NAME(vecMatProductnvn_start_64),set0_gf2n, LOOPIR_START_N,REM_START_NV,HFEnvq)
            if (HFEnvr == 0)
            {
                return;
            }
            bit_ir = vec.get(HFEnvq) >>> ir;
            loopir_param = HFEnvr;
            break;
        default:
            throw new IllegalArgumentException("Invalid input for vecMatProduct");
        }
        for (; ir < loopir_param; ++ir)
        {
            res.setXorRangeAndMask(0, S, 0, gf2_len, -(bit_ir & 1L));
            /* next row of S */
            S.move(S_cp_increase);
            bit_ir >>>= 1;
        }
        if (vecMatProduct == FunctionParams.M && HFEmr != 0)
        {
            res.setAnd(NB_WORD_GF2m - 1, MASK_GF2m);
        }
    }

    /**
     * @return The constant c of pk2, in GF(2).
     * @brief Decompression of a compressed MQ equation in GF(2)[x1,...,x_(n+v)].
     * Both use a lower triangular matrix.
     * @details pk = (c,Q), with c the constant part in GF(2) and Q is a lower
     * triangular matrix of size (n+v)*(n+v) in GF(2). pk2 will have the same
     * format, but the equation will be decompressed. Here, the last byte of pk is
     * padded with null bits.
     * @param[in] pk  A MQ equation in GF(2)[x1,...,x_(n+v)].
     * @param[out] pk2_orig A MQ equation in GF(2)[x1,...,x_(n+v)].
     * @remark Requires to allocate NB_WORD_UNCOMP_EQ 64-bit words for pk2.
     * @remark Requirement: at least NB_BYTES_EQUATION
     * + ((8-(NB_BYTES_EQUATION mod 8)) mod 8) bytes have to be allocated for pk
     * (because pk is cast in 64-bit, and the last memory access requires that
     * is allocated a multiple of 64 bits).
     * @remark Constant-time implementation.
     */
    private long convMQ_uncompressL_gf2(Pointer pk2_orig, PointerUnion pk)
    {
        int iq, ir, k, nb_bits;
        PointerUnion pk64 = new PointerUnion(pk);
        Pointer pk2 = new Pointer(pk2_orig);
        nb_bits = 1;
        pk2_orig.indexReset();
        /* For each row */
        for (iq = 0; iq < HFEnvq; ++iq)
        {
            for (ir = 1; ir < 64; ++ir)
            {
                if ((nb_bits & 63) != 0)
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, pk64.get(k) >>> (nb_bits & 63) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    if (((nb_bits & 63) + ir) > 64)
                    {
                        pk2.setXor(k, pk64.get(k + 1) << (64 - (nb_bits & 63)));
                    }
                    if (((nb_bits & 63) + ir) >= 64)
                    {
                        pk64.moveIncremental();
                    }
                }
                else
                {
                    for (k = 0; k <= iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                }
                pk64.move(iq);
                /* 0 padding on the last word */
                pk2.setAnd(iq, (1L << ir) - 1L);
                pk2.move(iq + 1);
                nb_bits += (iq << 6) + ir;
            }

            /* ir=64 */
            if ((nb_bits & 63) != 0)
            {
                for (k = 0; k <= iq; ++k)
                {
                    pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                }
            }
            else
            {
                for (k = 0; k <= iq; ++k)
                {
                    pk2.set(k, pk64.get(k));
                }
            }
            pk64.move(iq + 1);
            pk2.move(iq + 1);
            nb_bits += (iq + 1) << 6;
        }
        if (HFEnvr != 0)
        {
            for (ir = 1; ir <= HFEnvr; ++ir)
            {
                if ((nb_bits & 63) != 0)
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    if (((nb_bits & 63) + ir) > 64)
                    {
                        pk2.setXor(k, pk64.get(k + 1) << (64 - (nb_bits & 63)));
                    }

                    if (((nb_bits & 63) + ir) >= 64)
                    {
                        pk64.moveIncremental();
                    }
                }
                else
                {
                    for (k = 0; k <= iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                }
                pk64.move(iq);
                /* 0 padding on the last word */
                pk2.setAnd(iq, (1L << ir) - 1L);
                pk2.move(iq + 1);
                nb_bits += (iq << 6) + ir;
            }
        }
        /* Constant */
        return pk.get() & 1;
    }

    /**
     * @return The constant c of pk2, in GF(2).
     * @brief Decompression of a compressed MQ equation in GF(2)[x1,...,x_(n+v)].
     * Both use a lower triangular matrix.
     * @details pk = (c,Q), with c the constant part in GF(2) and Q is a lower
     * triangular matrix of size (n+v)*(n+v) in GF(2). pk2 will have the same
     * format, but the equation will be decompressed. Here, the last bits of pk
     * are missing (cf. the output of convMQ_last_UL_gf2). Moreover, the last byte
     * of pk is padded with null bits.
     * @param[in] pk  A MQ equation in GF(2)[x1,...,x_(n+v)].
     * @param[out] pk2 A MQ equation in GF(2)[x1,...,x_(n+v)].
     * @remark Requires to allocate NB_WORD_UNCOMP_EQ 64-bit words for pk2.
     * @remark This function is a modified copy of convMQ_uncompressL_gf2.
     * @remark Constant-time implementation.
     */
    private long convMQ_last_uncompressL_gf2(Pointer pk2, PointerUnion pk)
    {
        PointerUnion pk64 = new PointerUnion(pk);
        int iq, ir, k, nb_bits;
        nb_bits = 1;
        Pointer pk2_orig = new Pointer(pk2);
        pk2_orig.indexReset();
        final int HFEnvqm1 = (HFEnv - 1) >>> 6;
        /* For each row */
        for (iq = 0; iq < HFEnvqm1; ++iq)
        {
            for (ir = 1; ir < 64; ++ir)
            {
                if ((nb_bits & 63) != 0)
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    if (((nb_bits & 63) + ir) > 64)
                    {
                        pk2.setXor(k, pk64.get(k + 1) << (64 - (nb_bits & 63)));
                    }
                    if (((nb_bits & 63) + ir) >= 64)
                    {
                        pk64.moveIncremental();
                    }
                }
                else
                {
                    for (k = 0; k <= iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                }
                pk64.move(iq);
                /* 0 padding on the last word */
                pk2.setAnd(iq, (1L << ir) - 1L);
                pk2.move(iq + 1);
                nb_bits += (iq << 6) + ir;
            }
            /* ir=64 */
            if ((nb_bits & 63) != 0)
            {
                for (k = 0; k <= iq; ++k)
                {
                    pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                }
            }
            else
            {
                for (k = 0; k <= iq; ++k)
                {
                    pk2.set(k, pk64.get(k));
                }
            }
            pk64.move(iq + 1);
            pk2.move(iq + 1);
            nb_bits += (iq + 1) << 6;
        }
        final int HFEnvrm1 = (HFEnv - 1) & 63;
        if (HFEnvrm1 != 0)
        {
            for (ir = 1; ir <= HFEnvrm1; ++ir)
            {
                if ((nb_bits & 63) != 0)
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    if (((nb_bits & 63) + ir) > 64)
                    {
                        pk2.setXor(k, pk64.get(k + 1) << (64 - (nb_bits & 63)));
                    }

                    if (((nb_bits & 63) + ir) >= 64)
                    {
                        pk64.moveIncremental();
                    }
                }
                else
                {
                    for (k = 0; k <= iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                }
                pk64.move(iq);
                /* 0 padding on the last word */
                pk2.setAnd(iq, (1L << ir) - 1L);
                pk2.move(iq + 1);
                nb_bits += (iq << 6) + ir;
            }
        }
        /* Last row */
        /* The size of the last row is HFEnv-LOST_BITS bits */
        final int LAST_ROW_Q = ((HFEnv - LOST_BITS) >>> 6);
        final int LAST_ROW_R = ((HFEnv - LOST_BITS) & 63);
        iq = LAST_ROW_Q;
        long end;
        if (LAST_ROW_R != 0)
        {
            ir = LAST_ROW_R;
            if ((nb_bits & 63) != 0)
            {
                if ((((NB_MONOMIAL_PK - LOST_BITS + 7) >>> 3) & 7) != 0)
                {
                    final int NB_WHOLE_BLOCKS = ((HFEnv - ((64 - ((NB_MONOMIAL_PK - LOST_BITS - HFEnvr) & 63)) & 63)) >>> 6);
                    for (k = 0; k < NB_WHOLE_BLOCKS; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.getWithCheck(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.getWithCheck(k) >>> (nb_bits & 63));
                    if (NB_WHOLE_BLOCKS < LAST_ROW_Q)
                    {
                        end = pk64.getWithCheck(k + 1);
                        pk2.setXor(k, end << (64 - (nb_bits & 63)));
                        pk2.set(k + 1, end >>> (nb_bits & 63));
                    }
                    else
                    {
                        if (((nb_bits & 63) + ir) > 64)
                        {
                            pk2.setXor(k, pk64.getWithCheck(k + 1) << (64 - (nb_bits & 63)));
                        }
                    }
                }
                else
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    if (((nb_bits & 63) + ir) > 64)
                    {
                        pk2.setXor(k, pk64.get(k + 1) << (64 - (nb_bits & 63)));
                    }
                }
            }
            else
            {
                if ((((NB_MONOMIAL_PK - LOST_BITS + 7) >>> 3) & 7) != 0)
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                    pk2.set(k, pk64.getWithCheck(k));
                }
                else
                {
                    for (k = 0; k <= iq; ++k)
                    {
                        pk2.set(k, pk64.get(k));
                    }
                }
            }
        }
        else if (LAST_ROW_Q != 0)
        {
            if ((nb_bits & 63) != 0)
            {
                if ((((NB_MONOMIAL_PK - LOST_BITS + 7) >>> 3) & 7) != 0)
                {
                    for (k = 0; k < (iq - 1); ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                    pk2.set(k, pk64.get(k) >>> (nb_bits & 63));
                    pk2.setXor(k, pk64.getWithCheck(k + 1) << (64 - (nb_bits & 63)));
                }
                else
                {
                    for (k = 0; k < iq; ++k)
                    {
                        pk2.set(k, (pk64.get(k) >>> (nb_bits & 63)) ^ (pk64.get(k + 1) << (64 - (nb_bits & 63))));
                    }
                }
            }
            else
            {
                for (k = 0; k < iq; ++k)
                {
                    pk2.set(k, pk64.get(k));
                }
            }
        }
        Pointer pkPrint = new Pointer(pk2);
        pkPrint.indexReset();
        /* Constant */
        return pk.get() & 1L;
    }

    /**
     * @return 0 for a valid signature, !=0 else.
     * @brief Verify the signature of the document m of length len bytes, using a
     * (HFEv-)-based signature scheme. pk can be evaluated with the eval_pk
     * function, and hpk is used during this evaluation.
     * @details eval_pk takes 4 arguments here.
     * @param[in] m   A pointer on a document.
     * @param[in] len The length in bytes of the document m.
     * @param[in] sm8 A signature generated by a (HFEv-)-based signature scheme.
     * @param[in] pk  The original public-key, a MQ system with m equations in
     * GF(2)[x1,...,x_(n+v)].
     * @param[in] hpk The hybrid representation of one part of the public-key pk.
     * @remark Requirement: when SSE or AVX is enabled, the public-key must be
     * aligned respectively on 16 or 32 bytes. However, this requirement and the
     * alignment are disabled for the public/stable version of MQsoft (to be simple
     * to use, generic for the allocation of pk and to avoid segmentation faults).
     * @remark This function does not require a constant-time implementation.
     */
    public int sign_openHFE_huncomp_pk(byte[] m, int len, byte[] sm8, PointerUnion pk, PointerUnion hpk)
    {
        Pointer sm = new Pointer(SIZE_SIGN_UNCOMPRESSED - SIZE_SALT_WORD);
        int m_cp = 0;
        Pointer Si_tab = new Pointer(NB_WORD_GF2nv);
        Pointer Si1_tab = new Pointer(NB_WORD_GF2nv);
        long cst = 0; //if HFEmr8
        /* Copy of pointer */
        Pointer tmp;
        Pointer Si = new Pointer(Si_tab);
        Pointer Si1 = new Pointer(Si1_tab);
        /* Vector of D_1, ..., D_(NB_ITE) */
        PointerBuffer D = new PointerBuffer(NB_ITE * SIZE_DIGEST_UINT, 64);
        int i;
        int index;
        if (HFEmr8 != 0)
        {
            cst = hpk.get();
            /* We jump the constant (stored on 8 bytes) */
            hpk.move(1);
        }
        if (NB_ITE == 1)
        {
            /* Take the (n+v) first bits */
            System.arraycopy(sm8, 0, sm.toBytes(NB_BYTES_GFqnv), 0, NB_BYTES_GFqnv);
        }
        else
        {
            uncompress_signHFE(sm, sm8, 0);
        }
        SHA3Digest sha3Digest = new SHA3Digest(Sha3BitStrength);
        /* Compute H1 = H(m), the m first bits are D1 */
        sha3Digest.update(m, m_cp, len);
        sha3Digest.doFinal(D.getBuffer(), 0);
        D.bufferFill(0);
        for (i = 1; i < NB_ITE; ++i)
        {
            /* Compute Hi = H(H_(i-1)), the m first bits are Di */
            sha3Digest.reset();
            sha3Digest.update(D.getBuffer(), 0, SIZE_DIGEST);//(i - 1) * SIZE_DIGEST_UINT
            sha3Digest.doFinal(D.getBuffer(), 0);//i * SIZE_DIGEST_UINT
            D.bufferFill(i * SIZE_DIGEST_UINT);
            /* Clean the previous hash (= extract D_(i-1) from H_(i-1)) */
            if (HFEmr != 0)
            {
                D.setAnd(SIZE_DIGEST_UINT * (i - 1) + NB_WORD_GF2m - 1, MASK_GF2m);
            }
        }
        /* Clean the previous hash (= extract D_(i-1) from H_(i-1)) */
        if (HFEmr != 0)
        {
            D.setAnd(SIZE_DIGEST_UINT * (i - 1) + NB_WORD_GF2m - 1, MASK_GF2m);
        }
        /* Compute p(S_(NB_IT),X_(NB_IT)) */
        //TODO: bug when HFEmr8 is 0 use evalMQSnocst8_gf2 (evalMQSnocst8_unrolled_gf2) function
        evalMQShybrid8_uncomp_nocst_gf2_m(Si, sm, pk, hpk);
        if (HFEmr8 != 0)
        {
            Si.setXor(HFEmq, cst);
        }
        for (i = NB_ITE - 1; i > 0; --i)
        {
            /* Compute Si = xor(p(S_i+1,X_i+1),D_i+1) */
            Si.setXorRange(0, D, i * SIZE_DIGEST_UINT, NB_WORD_GF2m);
            /* Compute Si||Xi */
            index = NB_WORD_GF2nv + (NB_ITE - 1 - i) * NB_WORD_GF2nvm;
            if (HFEmr != 0)
            {
                Si.setAnd(NB_WORD_GF2m - 1, MASK_GF2m);
                /* Concatenation(Si,Xi): the intersection between S1 and X1 is not null */
                Si.setXor(NB_WORD_GF2m - 1, sm.get(index));
                if (NB_WORD_GF2nvm != 1)
                {
                    ++index;
                    Si.copyFrom(NB_WORD_GF2m, sm, index, NB_WORD_GF2nvm - 1);
                }
            }
            else
            {
                /* Concatenation(Si,Xi) */
                Si.copyFrom(NB_WORD_GF2m, sm, index, NB_WORD_GF2nvm);
            }
            /* Compute p(Si,Xi) */
            evalMQShybrid8_uncomp_nocst_gf2_m(Si1, Si, pk, hpk);
            if (HFEmr8 != 0)
            {
                Si1.setXor(HFEmq, cst);
            }
            /* Permutation of pointers */
            tmp = new Pointer(Si1);
            Si1.changeIndex(Si);
            Si.changeIndex(tmp);
        }
//        /* D1'' == D1 */
        return isEqual_nocst_gf2(Si, D, NB_WORD_GF2m) ? 1 : 0;
    }

    /**
     * @brief Variable-time evaluation of a MQS in a vector. The MQS is stored
     * with a hybrid representation.
     * @details The FORMAT_HYBRID_CPK8 have to be used. The (m-(m mod 8)) first
     * equations are stored as one multivariate quadratic equation in
     * GF(2^(m-(m mod 8)))[x1,...,x_(n+v)], i.e. the monomial representation is
     * used. This corresponds to mq_quo. The (m mod 8) last equations are stored
     * separately in mq_rem. Here, the EVAL_HYBRID_CPK8_UNCOMP have to be used, i.e.
     * the last equations are uncompressed.
     * mq_quo = (c',Q').
     * mq_rem = (c_(m-(m mod 8)),Q_(m-(m mod 8)),...,c_(m-1),Q_(m-1)).
     * c' is in GF(2^(m-(m mod 8))).
     * Q' is upper triangular of size (n+v)*(n+v) in GF(2^(m-(m mod 8))).
     * The (m mod 8) ci are in GF(2).
     * The (m mod 8) Qi are lower triangular of size (n+v)*(n+v) in GF(2).
     * For each Qi, the rows are stored separately (we take new words for each new
     * row).
     * @param[in] x   A vector of n+v elements in GF(2).
     * @param[in] mq_quo  The (m-(m mod 8)) first equations,
     * in GF(2^(m-(m mod 8)))[x1,...,x_(n+v)].
     * @param[in] mq_rem_orig  The (m mod 8) last equations,
     * in (GF(2)[x1,...,x_(n+v)])^(m mod 8).
     * @param[out] res A vector of m elements in GF(2), evaluation of the MQS in x.
     * @remark Requirement: at least ACCESS_last_equations8 + ((8-(HFEmq8 mod 8))
     * mod 8) bytes have to be allocated for mq_quo (because of the use of
     * evalMQSnocst8_quo_gf2).
     * @remark If a vector version of evalMQnocst_gf2 is used, maybe the last
     * vector load read outside of memory. So, if this load reads z bits, let
     * B be ceiling(z/64). The last equation requires NB_WORD_UNCOMP_EQ
     * + ((B-(NB_WORD_GF2nv mod B)) mod B) 64-bit words.
     * @remark Variable-time implementation.
     */
    private void evalMQShybrid8_uncomp_nocst_gf2_m(Pointer res, Pointer x, PointerUnion mq_quo, PointerUnion mq_rem_orig)
    {
        PointerUnion mq_rem = new PointerUnion(mq_rem_orig);
        evalMQSnocst8_quo_gf2(res, x, mq_quo);
        if (HFEmr8 != 0)
        {
            if (HFEmr < 8)
            {
                res.set(HFEmq, 0);
            }
            int i;
            for (i = HFEmr - HFEmr8; i < HFEmr; ++i)
            {
                res.setXor(HFEmq, evalMQnocst_unrolled_no_simd_gf2(x, mq_rem) << i);
                mq_rem.move(NB_WORD_UNCOMP_EQ);
            }
        }
    }

    /* Uncompress the signature */
    private void uncompress_signHFE(Pointer sm, byte[] sm8, int sm8_cp)
    {
        PointerUnion sm64 = new PointerUnion(sm);
        int k2;
        /* Take the (n+v) first bits */
        sm64.fillBytes(0, sm8, sm8_cp, NB_BYTES_GFqnv);
        /* Clean the last byte */
        if ((NB_ITE > 1) && HFEnvr8 != 0)
        {
            sm64.setAndByte(NB_BYTES_GFqnv - 1, MASK8_GF2nv);
        }
        /* Take the (Delta+v)*(nb_ite-1) bits */
        if (NB_ITE > 1)
        {
            int k1, nb_rem2, nb_rem_m, val_n;
            int nb_rem;
            /* HFEnv bits are already extracted from sm8 */
            int nb_bits = HFEnv;
            sm64.moveNextBytes((NB_WORD_GF2nv << 3) + (HFEmq8 & 7));
            for (k1 = 1; k1 < NB_ITE; ++k1)
            {
                /* Number of bits to complete the byte of sm8, in [0,7] */
                val_n = Math.min((HFEDELTA + HFEv), ((8 - (nb_bits & 7)) & 7));
                /* First byte of sm8 */
                if ((nb_bits & 7) != 0)
                {
                    if (HFEmr8 != 0)
                    {
                        sm64.setXorByte(((sm8[nb_bits >>> 3] & 0xFF) >>> (nb_bits & 7)) << HFEmr8);
                        /* Number of bits to complete the first byte of sm8 */
                        nb_rem = val_n - VAL_BITS_M;
                        if (nb_rem >= 0)
                        {
                            /* We take the next byte since we used VAL_BITS_M bits */
                            sm64.moveNextByte();
                        }
                        if (nb_rem > 0)
                        {
                            nb_bits += VAL_BITS_M;
                            sm64.setXorByte((sm8[nb_bits >>> 3] & 0xFF) >>> (nb_bits & 7));
                            nb_bits += nb_rem;
                        }
                        else
                        {
                            nb_bits += val_n;
                        }
                    }
                    else
                    {
                        /* We can take 8 bits, and we want at most 7 bits. */
                        sm64.setByte((sm8[nb_bits >>> 3] & 0xFF) >>> (nb_bits & 7));
                        nb_bits += val_n;
                    }
                }
                /* Other bytes of sm8 */
                nb_rem2 = (HFEDELTA + HFEv) - val_n;
                /*nb_rem2 can be zero only in this case */
                /* Number of bits used of sm64, mod 8 */
                nb_rem_m = (HFEm + val_n) & 7;
                /* Other bytes */
                if (nb_rem_m != 0)
                {
                    /* -1 to take the ceil of /8, -1 */
                    for (k2 = 0; k2 < ((nb_rem2 - 1) >>> 3); ++k2)
                    {
                        sm64.setXorByte((sm8[nb_bits >>> 3] & 0xFF) << nb_rem_m);
                        sm64.moveNextByte();
                        sm64.setXorByte((sm8[nb_bits >>> 3] & 0xFF) >>> (8 - nb_rem_m));

                        nb_bits += 8;
                    }
                    /* The last byte of sm8, between 1 and 8 bits to put */
                    sm64.setXorByte((sm8[nb_bits >>> 3] & 0xFF) << nb_rem_m);
                    sm64.moveNextByte();
                    /* nb_rem2 between 1 and 8 bits */
                    nb_rem2 = ((nb_rem2 + 7) & 7) + 1;
                    if (nb_rem2 > (8 - nb_rem_m))
                    {
                        sm64.setByte((sm8[nb_bits >>> 3] & 0xFF) >>> (8 - nb_rem_m));
                        sm64.moveNextByte();
                    }
                    nb_bits += nb_rem2;
                }
                else
                {
                    /* We are at the beginning of the bytes of sm8 and sm64 */
                    /* +7 to take the ceil of /8 */
                    for (k2 = 0; k2 < ((nb_rem2 + 7) >>> 3); ++k2)
                    {
                        sm64.setByte(sm8[nb_bits >>> 3]);
                        nb_bits += 8;
                        sm64.moveNextByte();
                    }
                    /* The last byte has AT MOST 8 bits. */
                    nb_bits -= (8 - (nb_rem2 & 7)) & 7;
                }
                /* Clean the last byte */
                if (HFEnvr8 != 0)
                {
                    sm64.setAndByte(-1, MASK8_GF2nv);
                }
                /* We complete the word. Then we search the first byte. */
                sm64.moveNextBytes(((8 - (NB_BYTES_GFqnv & 7)) & 7) + (HFEmq8 & 7));
            }
        }
    }

    private void evalMQSnocst8_quo_gf2(Pointer c, Pointer m, PointerUnion pk_orig)
    {
        long xi, xj;
        int iq, ir, i = HFEnv, jq;
        final int NB_EQ = (HFEm >>> 3) != 0 ? ((HFEm >>> 3) << 3) : HFEm;
        final int NB_BYTES_EQ = (NB_EQ & 7) != 0 ? ((NB_EQ >>> 3) + 1) : (NB_EQ >>> 3);
        final int NB_WORD_EQ = (NB_BYTES_EQ >>> 3) + ((NB_BYTES_EQ & 7) != 0 ? 1 : 0);
        /* Constant cst_pk */
        PointerUnion pk = new PointerUnion(pk_orig);
        System.arraycopy(pk.getArray(), 0, c.getArray(), c.getIndex(), NB_WORD_EQ);
        pk.moveNextBytes(NB_BYTES_EQ);
        /* for each row of the quadratic matrix of pk, excepted the last block */
        for (iq = 0; iq < HFEnvq; ++iq)
        {
            xi = m.get(iq);
            for (ir = 0; ir < NB_BITS_UINT; ++ir, --i)
            {
                if ((xi & 1) != 0)
                {
                    /* for each column of the quadratic matrix of pk */
                    /* xj=xi=1 */
                    c.setXorRange(0, pk, 0, NB_WORD_EQ);
                    pk.moveNextBytes(NB_BYTES_EQ);
                    xj = xi >>> 1;
                    LOOPJR_UNROLLED_64(c, pk, ir + 1, NB_BITS_UINT, xj, NB_BYTES_EQ, NB_WORD_EQ);
                    for (jq = iq + 1; jq < HFEnvq; ++jq)
                    {
                        xj = m.get(jq);
                        LOOPJR_UNROLLED_64(c, pk, 0, NB_BITS_UINT, xj, NB_BYTES_EQ, NB_WORD_EQ);
                    }
                    if ((HFEnvr) != 0)
                    {
                        xj = m.get(HFEnvq);
                        if (HFEnvr < (LEN_UNROLLED_64 << 1))
                        {
                            LOOPJR_NOCST_64(c, pk, 0, HFEnvr, xj, NB_BYTES_EQ, NB_WORD_EQ);
                        }
                        else
                        {
                            LOOPJR_UNROLLED_64(c, pk, 0, HFEnvr, xj, NB_BYTES_EQ, NB_WORD_EQ);
                        }
                    }
                }
                else
                {
                    pk.moveNextBytes(i * NB_BYTES_EQ);
                }
                xi >>>= 1;
            }
        }
        /* the last block */
        if (HFEnvr != 0)
        {
            xi = m.get(HFEnvq);
            for (ir = 0; ir < HFEnvr; ++ir, --i)
            {
                if ((xi & 1) != 0)
                {
                    /* for each column of the quadratic matrix of pk */
                    /* xj=xi=1 */
                    c.setXorRange(0, pk, 0, NB_WORD_EQ);
                    pk.moveNextBytes(NB_BYTES_EQ);
                    xj = xi >>> 1;
                    if (HFEnvr < (LEN_UNROLLED_64 << 1))
                    {
                        LOOPJR_NOCST_64(c, pk, ir + 1, HFEnvr, xj, NB_BYTES_EQ, NB_WORD_EQ);
                    }
                    else
                    {
                        LOOPJR_UNROLLED_64(c, pk, ir + 1, HFEnvr, xj, NB_BYTES_EQ, NB_WORD_EQ);
                    }
                }
                else
                {
                    pk.moveNextBytes(i * NB_BYTES_EQ);
                }
                xi >>>= 1;
            }
        }
        MASK_64(c, NB_WORD_EQ - 1, NB_EQ);
    }

    private void MASK_64(Pointer c, int p, int NB_EQ)
    {
        if ((NB_EQ & 63) != 0)
        {
            c.setAnd(p, (1L << (NB_EQ & 63)) - 1L);
        }
    }

    private void LOOPJR_UNROLLED_64(Pointer c, PointerUnion pk64, int START, int NB_IT, long xj, int NB_BYTES_EQ, int NB_WORD_EQ)
    {
        int jr, h;
        for (jr = START; jr < (NB_IT - LEN_UNROLLED_64 + 1); jr += LEN_UNROLLED_64)
        {
            for (h = 0; h < LEN_UNROLLED_64; ++h)
            {
                if ((xj & 1L) != 0)
                {
                    c.setXorRange(0, pk64, 0, NB_WORD_EQ);
                }
                pk64.moveNextBytes(NB_BYTES_EQ);
                xj >>>= 1;
            }
        }
        for (; jr < NB_IT; ++jr)
        {
            if ((xj & 1L) != 0)
            {
                c.setXorRange(0, pk64, 0, NB_WORD_EQ);
            }
            pk64.moveNextBytes(NB_BYTES_EQ);
            xj >>>= 1;
        }
    }

    private void LOOPJR_NOCST_64(Pointer c, PointerUnion pk64, int START, int NB_IT, long xj, int NB_BYTES_EQ, int NB_WORD_EQ)
    {
        //int len = (NB_BYTES_EQ >>> 3) + ((NB_BYTES_EQ & 7) != 0 ? 1 : 0);
        for (int jr = START; jr < NB_IT; ++jr)
        {
            if ((xj & 1) != 0)
            {
                c.setXorRange(0, pk64, 0, NB_WORD_EQ);
            }
            pk64.moveNextBytes(NB_BYTES_EQ);
            xj >>>= 1;
        }
    }

    private long evalMQnocst_unrolled_no_simd_gf2(Pointer m, PointerUnion mq_orig)
    {
        long acc = 0;
        int i;
        int loop_end = ((NB_WORD_GF2nv == 1) && (HFEnvr != 0)) ? HFEnvr : 64;
        PointerUnion mq = new PointerUnion(mq_orig);
        for (i = 0; i < loop_end; ++i)
        {
            if (((m.get() >>> i) & 1L) != 0)
            {
                acc ^= mq.get(i) & m.get();
            }
        }
        mq.move(64);
        for (int j = 1; j < NB_WORD_GF2nv; ++j)
        {
            loop_end = (NB_WORD_GF2nv == (j + 1) && HFEnvr != 0) ? HFEnvr : 64;
            for (i = 0; i < loop_end; ++i)
            {
                if (((m.get(j) >>> i) & 1) != 0)
                {
                    for (int k = 0; k <= j; ++k)
                    {
                        acc ^= mq.get(k) & m.get(k);
                    }
                }
                mq.move(j + 1);
            }
        }
        acc = GeMSSUtils.XORBITS_UINT(acc);
        return acc;
    }

    private boolean isEqual_nocst_gf2(Pointer a, Pointer b, int len)
    {
        for (int i = 0; i < len; ++i)
        {
            if (a.get(i) != b.get(i))
            {
                return false;
            }
        }
        return true;
    }

    public int signHFE_FeistelPatarin(SecureRandom random, byte[] sm8, byte[] m, int m_cp, int len, byte[] sk)
    {
        this.random = random;
        Pointer U = new Pointer(NB_WORD_GFqn);
        Pointer Hi_tab = new Pointer(SIZE_DIGEST_UINT);
        Pointer Hi1_tab = new Pointer(SIZE_DIGEST_UINT);
        Pointer tmp = new Pointer();
        Pointer Hi1 = new Pointer(Hi1_tab);
        final int HFEvr8 = HFEv & 7;
        /* Number of bytes that an element of GF(2^(n+v)) needs */
        final int NB_BYTES_GFqv = (HFEv >>> 3) + ((HFEvr8 != 0) ? 1 : 0);
        final long HFE_MASKv = maskUINT(HFEvr);
        int k;
        int index;
        long rem_char = 0;
        int nb_root;
        SecretKeyHFE sk_HFE = new SecretKeyHFE(this);
        Pointer F;
        int i;
        Pointer V = new Pointer(NB_WORD_GFqv);
        Pointer[] linear_coefs = new Pointer[HFEDegI + 1];
        nb_root = precSignHFE(sk_HFE, linear_coefs, sk);
        if (nb_root != 0)
        {
            /* Error from malloc */
            return nb_root;
        }
        F = new Pointer(sk_HFE.F_struct.poly);
        /* Compute H1 = H(m) */
        PointerBuffer Hi = new PointerBuffer(Hi_tab, Sha3BitStrength / 8);
        SHA3Digest sha3Digest = new SHA3Digest(Sha3BitStrength);
        sha3Digest.update(m, m_cp, len);
        sha3Digest.doFinal(Hi.getBuffer(), 0);
        Hi.bufferFill(0);
        /* It is to initialize S0 to 0, because Sk||Xk is stored in sm */
        Pointer sm = new Pointer(SIZE_SIGN_UNCOMPRESSED - SIZE_SALT_WORD);
        Pointer DR = new Pointer(NB_WORD_GF2nv);
        PointerUnion DR_cp = new PointerUnion(DR);
        sm.setRangeClear(0, NB_WORD_GF2nv);
        DR.setRangeClear(0, NB_WORD_GF2nv);
        for (k = 1; k <= NB_ITE; ++k)
        {
            /* Compute xor(D_k,S_(k-1)) */
            DR.setRangeFromXor(0, sm, 0, Hi, 0, NB_WORD_GF2m);
            if ((HFEm & 7) != 0)
                /* Clean the last char to compute rem_char (the last word is cleaned) */
            {
                DR.setAnd(NB_WORD_GF2m - 1, MASK_GF2m);
                /* Save the last byte because we need to erase this value by randombytes */
                rem_char = DR_cp.getByte(HFEmq8);//NB_BYTES_GFqm - 1, Since
            }
            /* When the root finding fails, the minus and vinegars are regenerated */
            do
            {
                /* Compute Dk||Rk: add random to have n bits, without erased the m bits */
                if ((HFEm & 7) != 0)
                {
                    /* Generation of Rk */
                    DR_cp.fillRandomBytes(HFEmq8, random, NB_BYTES_GFqn - NB_BYTES_GFqm + 1);
                    /* Put HFEm&7 first bits to 0 */
                    DR_cp.setAndByte(HFEmq8, -(1 << (HFEm & 7)));
                    /* Store rem_char */
                    DR_cp.setXorByte(HFEmq8, rem_char);
                }
                else
                {
                    DR_cp.fillRandomBytes(NB_BYTES_GFqm, random, NB_BYTES_GFqn - NB_BYTES_GFqm);
                }
                /* To clean the last char (because of randombytes), the last word is cleaned */
                if ((HFEn & 7) != 0)
                {
                    DR.setAnd(NB_WORD_GFqn - 1, MASK_GF2n);
                }
                /* Compute Sk||Xk = Inv_p(Dk,Rk) */
                /* Firstly: compute c * T^(-1) */
                vecMatProduct(U, DR, sk_HFE.T, 0, FunctionParams.N);
                V.fillRandom(0, random, NB_BYTES_GFqv);
                if (HFEvr8 != 0)
                {
                    /* Clean the last word */
                    V.setAnd(NB_WORD_GFqv - 1, HFE_MASKv);
                }
                /* Evaluation of the constant, quadratic map with v vinegars */
                evalMQSv_unrolled_gf2(F, V, sk_HFE.F_HFEv);
                if (ENABLED_REMOVE_ODD_DEGREE)
                {
                    for (i = 0; i <= HFEDegI; ++i)
                    {
                        vecMatProduct(Buffer_NB_WORD_GFqn, V, new Pointer(linear_coefs[i], NB_WORD_GFqn), 0, FunctionParams.V);//tmp_n
                        F.setRangeFromXor(NB_WORD_GFqn * (((i * (i + 1)) >>> 1) + 1), linear_coefs[i], 0, Buffer_NB_WORD_GFqn, 0, NB_WORD_GFqn);//tmp_n
                    }
                }
                else
                {
                    for (i = 0; i <= HFEDegI; ++i)
                    {
                        vecMatProduct(Buffer_NB_WORD_GFqn, V, new Pointer(linear_coefs[i], NB_WORD_GFqn), 0, FunctionParams.V);//tmp_n
                        F.setRangeFromXor(NB_WORD_GFqn * (((i * (i + 1)) >>> 1) + 1), linear_coefs[i], 0, Buffer_NB_WORD_GFqn, 0, NB_WORD_GFqn);//tmp_n
                    }
                }
                nb_root = chooseRootHFE_gf2nx(DR, sk_HFE.F_struct, U);
                if (nb_root == 0)
                {
                    /* fail: retry with an other Rk */
                    continue;
                }
                if (nb_root < 0)
                {
                    /* Error from chooseRootHFE */
                    return nb_root;
                }
                break;
            }
            while (true);
            /* Add the v bits to DR */
            DR.setXor(NB_WORD_GFqn - 1, V.get() << HFEnr);
            DR.setRangeRotate(NB_WORD_GFqn, V, 0, NB_WORD_GFqv - 1, 64 - HFEnr);
            if (NB_WORD_GFqn + NB_WORD_GFqv == NB_WORD_GF2nv)
            {
                DR.set(NB_WORD_GFqn + NB_WORD_GFqv - 1, V.get(NB_WORD_GFqv - 1) >>> (64 - HFEnr));
            }
            /* Finally: compute Sk||Xk = v * S^(-1) */
            vecMatProduct(sm, DR, sk_HFE.S, 0, FunctionParams.NV);
            if (k != NB_ITE)
            {
                /* Store X1 in the signature */
                index = NB_WORD_GF2nv + (NB_ITE - 1 - k) * NB_WORD_GF2nvm;
                sm.copyFrom(index, sm, NB_WORD_GF2nv - NB_WORD_GF2nvm, NB_WORD_GF2nvm);
                //copy_gf2nvm(sm + index, sm + NB_WORD_GF2nv - NB_WORD_GF2nvm);
                /* To put zeros at the beginning of the first word of X1 */
                if (HFEmr != 0)
                {
                    sm.setAnd(index, ~MASK_GF2m);
                }
                /* Compute H2 = H(H1) */
                byte[] Hi_bytes = Hi.toBytes(SIZE_DIGEST);
                sha3Digest.update(Hi_bytes, 0, Hi_bytes.length);
                byte[] Hi1_bytes = new byte[SIZE_DIGEST];
                sha3Digest.doFinal(Hi1_bytes, 0);
                Hi1.fill(0, Hi1_bytes, 0, SIZE_DIGEST);
                /* Permutation of pointers */
                tmp.changeIndex(Hi1);
                Hi1.changeIndex(Hi);
                Hi.changeIndex(tmp);
            }
        }
        if (NB_ITE == 1)
        {
            /* Take the (n+v) first bits */
            byte[] sm64 = sm.toBytes(sm.getLength() << 3);
            System.arraycopy(sm64, 0, sm8, 0, NB_BYTES_GFqnv);
        }
        else
        {
            compress_signHFE(sm8, sm);
        }
        return 0;
    }

    /* Precomputation for one secret-key */
    private int precSignHFE(SecretKeyHFE sk_HFE, Pointer[] linear_coefs, byte[] sk)
    {
        Pointer F_cp;
        int i, j;
        precSignHFESeed(sk_HFE, sk);
        initListDifferences_gf2nx(sk_HFE.F_struct.L);
        Pointer F_HFEv = new Pointer(sk_HFE.F_HFEv);
        final int NB_UINT_HFEPOLY = NB_COEFS_HFEPOLY * NB_WORD_GFqn;
        Pointer F = new Pointer(NB_UINT_HFEPOLY);
        /* X^(2^0) */
        linear_coefs[0] = new Pointer(F_HFEv, MQv_GFqn_SIZE);
        /* X^(2^1) */
        F_HFEv.changeIndex(linear_coefs[0], MLv_GFqn_SIZE);
        F_cp = new Pointer(F, 2 * NB_WORD_GFqn);
        for (i = 0; i < HFEDegI; ++i)
        {
            /* Copy i quadratic terms */
            if (ENABLED_REMOVE_ODD_DEGREE)
            {
                j = (((1 << i) + 1) <= HFE_odd_degree) ? 0 : 1;
            }
            else
            {
                j = 0;
            }
            F_cp.copyFrom(F_HFEv, (i - j) * NB_WORD_GFqn);
            j = i - j;
            F_HFEv.move(j * NB_WORD_GFqn);
            F_cp.move(j * NB_WORD_GFqn);
            /* Store the address of X^(2^(i+1)) */
            linear_coefs[i + 1] = new Pointer(F_HFEv);
            /* Linear term is not copied */
            F_HFEv.move(MLv_GFqn_SIZE);
            F_cp.move(NB_WORD_GFqn);
        }
        if (HFEDegJ != 0)
        {
            /* X^(2^HFEDegI + 2^j) */
            //fgemss192 and fgemss256
            j = (((1 << i) + 1) <= HFE_odd_degree) ? 0 : 1;
            F_cp.copyFrom(F_HFEv, (HFEDegJ - j) * NB_WORD_GFqn);
        }
        sk_HFE.F_struct.poly = new Pointer(F);
        return 0;
    }

    private void precSignHFESeed(SecretKeyHFE sk_HFE, byte[] sk)
    {
        Pointer L, U;
        sk_HFE.sk_uncomp = new Pointer(NB_UINT_HFEVPOLY + (LTRIANGULAR_NV_SIZE << 1) + (LTRIANGULAR_N_SIZE << 1) + SIZE_VECTOR_t + MATRIXnv_SIZE + MATRIXn_SIZE);
        SHAKEDigest shakeDigest = new SHAKEDigest(ShakeBitStrength);
        shakeDigest.update(sk, 0, SIZE_SEED_SK);
        byte[] sk_uncomp_byte = new byte[(NB_UINT_HFEVPOLY + (LTRIANGULAR_NV_SIZE << 1) + (LTRIANGULAR_N_SIZE << 1) + SIZE_VECTOR_t) << 3];//<< 3
        shakeDigest.doFinal(sk_uncomp_byte, 0, sk_uncomp_byte.length);
        sk_HFE.sk_uncomp.fill(0, sk_uncomp_byte, 0, sk_uncomp_byte.length);
        sk_HFE.S = new Pointer(sk_HFE.sk_uncomp, NB_UINT_HFEVPOLY + (LTRIANGULAR_NV_SIZE << 1) + (LTRIANGULAR_N_SIZE << 1) + SIZE_VECTOR_t);
        sk_HFE.T = new Pointer(sk_HFE.S, MATRIXnv_SIZE);
        /* zero padding for the HFEv polynomial F */
        sk_HFE.F_HFEv = new Pointer(sk_HFE.sk_uncomp);
        cleanMonicHFEv_gf2nx(sk_HFE.F_HFEv);
        /* The random bytes are already generated from a seed */
        L = new Pointer(sk_HFE.sk_uncomp, NB_UINT_HFEVPOLY);
        U = new Pointer(L, LTRIANGULAR_NV_SIZE);
        cleanLowerMatrix(L, FunctionParams.NV);
        cleanLowerMatrix(U, FunctionParams.NV);
        /* Generate S^(-1) = L*U */
        mulMatricesLU_gf2(sk_HFE.S, L, U, FunctionParams.NV);
        /* The random bytes are already generated from a seed */
        L.move(LTRIANGULAR_NV_SIZE << 1);
        U.changeIndex(L, LTRIANGULAR_N_SIZE);
        cleanLowerMatrix(L, FunctionParams.N);
        cleanLowerMatrix(U, FunctionParams.N);
        /* Generate T^(-1) = L*U */
        mulMatricesLU_gf2(sk_HFE.T, L, U, FunctionParams.N);
    }

    void cleanMonicHFEv_gf2nx(Pointer F)
    {
        /* zero padding for the last word of each element of GF(2^n) */
        for (int F_idx = NB_WORD_GFqn - 1; F_idx < NB_UINT_HFEVPOLY; F_idx += NB_WORD_GFqn)
        {
            F.setAnd(F_idx, MASK_GF2n);
        }
    }

    private void mulMatricesLU_gf2(Pointer S, Pointer L, Pointer U, FunctionParams functionParams)
    {
        final int nq, nr;
        int iq;
        boolean REM;
        int S_orig = S.getIndex();
        switch (functionParams)
        {
        case N:
            nq = HFEnq;
            nr = HFEnr;
            REM = true;
            break;
        case NV:
            nq = HFEnvq;
            nr = HFEnvr;
            REM = HFEnvr != 0;
            break;
        default:
            throw new IllegalArgumentException("Invalid parameter for MULMATRICESLU_GF2");
        }
        /* Computation of S = L*U */
        Pointer L_cp = new Pointer(L);
        /* for each row of L (and S) */
        for (iq = 1; iq <= nq; ++iq)
        {
            LOOPIR(S, L_cp, U, NB_BITS_UINT, nq, nr, iq, REM);
        }
        LOOPIR(S, L_cp, U, nr, nq, nr, iq, REM);
        S.changeIndex(S_orig);
    }

    private void LOOPIR(Pointer S, Pointer L_cp, Pointer U, int NB_IT, int nq, int nr, int iq, boolean REM)
    {
        int jq;
        for (int ir = 0; ir < NB_IT; ++ir)
        {
            Pointer U_cp = new Pointer(U);
            /* for each row of U (multiply by the transpose) */
            for (jq = 1; jq <= nq; ++jq)
            {
                LOOPJR(S, L_cp, U_cp, NB_BITS_UINT, iq, jq);
            }
            if (REM)
            {
                LOOPJR(S, L_cp, U_cp, nr, iq, jq);
            }
            L_cp.move(iq);
        }
    }

    private void LOOPJR(Pointer S, Pointer L, Pointer U, int NB_IT, int iq, int jq)
    {
        int mini = Math.min(iq, jq);
        S.set(0, 0);
        long tmp;
        for (int jr = 0; jr < NB_IT; ++jr)
        {
            /* Dot product */
            tmp = L.getDotProduct(0, U, 0, mini);
            tmp = GeMSSUtils.XORBITS_UINT(tmp);
            S.setXor(tmp << jr);
            U.move(jq);
        }
        S.moveIncremental();
    }

    private void initListDifferences_gf2nx(Pointer L)
    {
        int i, j, k = 2;
        L.set(0);
        final long NB_WORD_GFqn_long = NB_WORD_GFqn;
        L.set(1, NB_WORD_GFqn_long);
        for (i = 0; i < HFEDegI; ++i)
        {
            if (ENABLED_REMOVE_ODD_DEGREE)
            {
                if (((1 << i) + 1) <= HFE_odd_degree)
                {
                    /* j=0 */
                    L.set(k, NB_WORD_GFqn_long);
                    ++k;
                    /* j=1 to j=i */
                    for (j = 0; j < i; ++j)
                    {
                        L.set(k, NB_WORD_GFqn_long << j);
                        ++k;
                    }
                }
                else
                {
                    /* j=0 */
                    if (i != 0)
                    {
                        L.set(k, NB_WORD_GFqn_long << 1);
                        ++k;
                    }
                    /* j=1 to j=i */
                    for (j = 1; j < i; ++j)
                    {
                        L.set(k, NB_WORD_GFqn_long << j);
                        ++k;
                    }
                }
            }
            else
            {
                /* j=0 */
                L.set(k, NB_WORD_GFqn);
                ++k;
                /* j=1 to j=i */
                for (j = 0; j < i; ++j)
                {
                    L.set(k, NB_WORD_GFqn_long << j);
                    ++k;
                }
            }
        }
        if (HFEDegJ != 0)
        {
            if (ENABLED_REMOVE_ODD_DEGREE)
            {
                if (((1 << i) + 1) <= HFE_odd_degree)
                {
                    /* j=0 */
                    L.set(k, NB_WORD_GFqn_long);
                    ++k;
                    /* j=1 to j=i */
                    for (j = 0; j < (HFEDegJ - 1); ++j)
                    {
                        L.set(k, NB_WORD_GFqn_long << j);
                        ++k;
                    }
                }
                else
                {
                    /* j=0 */
                    L.set(k, NB_WORD_GFqn_long << 1);
                    ++k;
                    /* j=1 to j=i */
                    for (j = 1; j < (HFEDegJ - 1); ++j)
                    {
                        L.set(k, NB_WORD_GFqn_long << j);
                        ++k;
                    }
                }
            }
            else
            {
                /* j=0*/
                L.set(k, NB_WORD_GFqn_long);
                ++k;
                /* j=1 to j=HFEDegJ-1 */
                for (j = 0; j < (HFEDegJ - 1); ++j)
                {
                    L.set(k, NB_WORD_GFqn_long << j);
                    ++k;
                }
            }
        }
    }

    /* Input:
    m a vector of n+v elements of GF(2)
    pk a MQ system with m equations in GF(2)[x1,...,x_(n+v)]
  Output:
    c a vector of m elements of GF(2), c is the evaluation of pk in m
    */
    private void evalMQSv_classical_gf2(Pointer c, Pointer m, Pointer pk)
    {
        long xi;
        Pointer x = new Pointer(HFEv);
        final int NB_VARq = HFEv >>> 6;
        final int NB_VARr = HFEv & 63;
        final int NB_WORD_EQ = (HFEn >>> 6) + ((HFEn & 63) != 0 ? 1 : 0);
        Pointer tmp = new Pointer(NB_WORD_EQ);
        int pk_orig = pk.getIndex();
        int i, j, k;
        /* Compute one time all -((xi>>1)&UINT_1) */
        k = 0;
        for (i = 0; i < NB_VARq; ++i)
        {
            xi = m.get(i);
            for (j = 0; j < NB_BITS_UINT; ++j, ++k)
            {
                x.set(k, -((xi >>> j) & 1L));
            }
        }
        if (NB_VARr != 0)
        {
            xi = m.get(i);
            for (j = 0; j < NB_VARr; ++j, ++k)
            {
                x.set(k, -((xi >>> j) & 1L));
            }
        }
        /* Constant cst_pk */
        c.copyFrom(pk, NB_WORD_EQ);
        pk.move(NB_WORD_EQ);
        /* for each row of the quadratic matrix of pk, excepted the last block */
        for (i = 0; i < HFEv; ++i)
        {
            /* for each column of the quadratic matrix of pk */
            /* xj=xi */
            tmp.copyFrom(pk, NB_WORD_EQ);
            pk.move(NB_WORD_EQ);
            for (j = i + 1; j < HFEv; ++j)
            {
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j));
                pk.move(NB_WORD_EQ);
            }
            /* Multiply by xi */
            c.setXorRangeAndMask(0, tmp, 0, NB_WORD_EQ, x.get(i));
        }
        pk.changeIndex(pk_orig);
    }

    void evalMQSv_unrolled_gf2(Pointer c, Pointer m, Pointer pk)
    {
        long xi;
        Pointer x = new Pointer(HFEv);
        final int NB_VARq = HFEv >>> 6;
        final int NB_VARr = HFEv & 63;
        final int NB_WORD_EQ = (HFEn >>> 6) + ((HFEn & 63) != 0 ? 1 : 0);
        int pk_orig = pk.getIndex();
        Pointer tmp = new Pointer(NB_WORD_EQ);
        int i, j, k;
        /* Compute one time all -((xi>>1)&UINT_1) */
        for (i = 0, k = 0; i < NB_VARq; ++i)
        {
            xi = m.get(i);
            for (j = 0; j < NB_BITS_UINT; ++j, ++k)
            {
                x.set(k, -((xi >>> j) & 1L));
            }
        }
        if (NB_VARr != 0)
        {
            xi = m.get(i);
            for (j = 0; j < NB_VARr; ++j, ++k)
            {
                x.set(k, -((xi >>> j) & 1L));
            }
        }
        /* Constant cst_pk */
        c.copyFrom(pk, NB_WORD_EQ);
        pk.move(NB_WORD_EQ);
        /* for each row of the quadratic matrix of pk, excepted the last block */
        for (i = 0; i < HFEv; ++i)
        {
            /* for each column of the quadratic matrix of pk */
            /* xj=xi */
            tmp.copyFrom(pk, NB_WORD_EQ);
            pk.move(NB_WORD_EQ);
            for (j = i + 1; j < HFEv - 3; j += 4)
            {
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j));
                pk.move(NB_WORD_EQ);
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j + 1));
                pk.move(NB_WORD_EQ);
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j + 2));
                pk.move(NB_WORD_EQ);
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j + 3));
                pk.move(NB_WORD_EQ);
            }
            for (; j < HFEv; ++j)
            {
                tmp.setXorRangeAndMask(0, pk, 0, NB_WORD_EQ, x.get(j));
                pk.move(NB_WORD_EQ);
            }
            /* Multiply by xi */
            c.setXorRangeAndMask(0, tmp, 0, NB_WORD_EQ, x.get(i));
        }
        pk.changeIndex(pk_orig);
    }

    private int chooseRootHFE_gf2nx(Pointer root, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer U)
    {
        Pointer hash = new Pointer(SIZE_DIGEST_UINT);
        int i, l;
        Pointer32 roots = new Pointer32();
        //l = findRootsHFE_gf2nx(roots, F, U);
        Pointer tmp_p, poly, poly2;
        poly = new Pointer(((HFEDeg << 1) - 1) * NB_WORD_GFqn);
        poly2 = new Pointer((HFEDeg + 1) * NB_WORD_GFqn);
        /* X^(2^n) - X mod (F-U) */
        if (HFEDeg <= 34 || (HFEn > 196 && HFEDeg < 256))
        {
            //2nd condition HFEDeg<=34
            //redgemss128, redgemss192, redgemss256, magentagemss128, magentagemss192, magentagemss256
            //3rd condition: HFEn>196
            // //bluegemss192, bluegemss256, cyangemss192, cyangemss256, fgemss128, dualmodems128, dualmodems192, dualmodems256
            //System.out.println(" frobeniusMap_multisqr_HFE_gf2nx Branch 2 " + HFEDeg + " " + HFEn);
            l = frobeniusMap_multisqr_HFE_gf2nx(poly, F, U);
        }
        else
        {
            //1st condition HFEDeg>=256
            //gemss128, gemss192, gemss256, whitegemss128, whitegemss192, whitegemss256, fgemss192, fgemss256
            //bluegemss128, cyangemss128
            l = frobeniusMap_HFE_gf2nx(poly, F, U);
        }
        /* Initialize to F */
        convHFEpolynomialSparseToDense_gf2nx(poly2, F);
        /* Initialize to F-U */
        poly2.setXorRange(0, U, 0, NB_WORD_GFqn);
        //add2_gf2(poly2, U, NB_WORD_GFqn);
        /* GCD(F-U, X^(2^n)-X mod (F-U)) */
        l = gcd_gf2nx(poly2, HFEDeg, poly, l);//d2
        i = buffer;
        if (i != 0)
        {
            tmp_p = poly;
            poly = poly2;
            poly2 = tmp_p;
        }
        if (poly.is0_gf2n(0, NB_WORD_GFqn) == 0)
        {
            /* The gcd is a constant (!=0) */
            /* Irreducible: 0 root */
            /* l=0; */
            l = 0;
        }
        else
        {
            /* poly2 is the gcd */
            /* Here, it becomes monic */
            convMonic_gf2nx(poly2, l);
            roots = new Pointer32(l * NB_WORD_GFqn);
            findRootsSplit_gf2nx(roots, poly2, l);
        }
        if (l == 0)
        {
            /* Zero root */
            return 0;
        }
        else
        {
            if (l == 1)
            {
                /* One root */
                root.copyFrom(roots, NB_WORD_GFqn);
            }
            else
            {
                /* Sort the roots */
                //selectionSort_gf2n(roots, l);
                fast_sort_gf2n(roots, l);
                /* Choose a root with a determinist hash */
                SHA3Digest sha3Digest = new SHA3Digest(Sha3BitStrength);
                byte[] U_bytes = U.toBytes(NB_BYTES_GFqn);
                byte[] hash_bytes = new byte[Sha3BitStrength >>> 3];
                sha3Digest.update(U_bytes, 0, U_bytes.length);
                sha3Digest.doFinal(hash_bytes, 0);
                hash.fill(0, hash_bytes, 0, hash_bytes.length);
                root.copyFrom(0, roots, (int)Long.remainderUnsigned(hash.get(), l) * NB_WORD_GFqn, NB_WORD_GFqn);
            }
            return l;
        }
    }

    private int gcd_gf2nx(Pointer A, int da, Pointer B, int db)
    {
        Pointer inv = new Pointer(NB_WORD_GFqn);
        Pointer tmp;
        int i;
        /* *b = 0: B is the last remainder
         *b = 1: A is the last remainder */
        buffer = 0;
        while (db != 0)
        {
            /* Computation of A = A mod B, of degree da */
            /* Minimizes the number of multiplications by an inverse */
            /* 2db > da */
            if ((db << 1) > da)
            {
                /* At most da-db+1 multiplications by an inverse */
                da = div_r_gf2nx(A, da, B, db);
            }
            else
            {
                /* B becomes monic: db multiplications by an inverse */
                inv_gf2n(inv, B, db * NB_WORD_GFqn);
                B.set1_gf2n(db * NB_WORD_GFqn, NB_WORD_GFqn);
                for (i = db - 1; i != -1; --i)
                {
                    mul_gf2n(B, i * NB_WORD_GFqn, B, i * NB_WORD_GFqn, inv, 0);
                }
                da = div_r_monic_gf2nx(A, da, B, db);
            }
            /* Swaps A and B */
            tmp = A;
            A = B;
            B = tmp;
            /* Swaps da and db */
            int tmp_word = da;
            da = db;
            db = tmp_word;
            /* 0 becomes 1 and 1 becomes 0 */
            buffer = 1 - buffer;
        }
        return da;
    }

    private int frobeniusMap_HFE_gf2nx(Pointer Xqn, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer U)
    {
        Pointer cst = new Pointer(NB_WORD_GFqn);
        long b, mask;
        int d, i;
        /* Constant term of F-U */
        cst.setRangeFromXor(0, F.poly, 0, U, 0, NB_WORD_GFqn);
        /* For i=HFEDegI, we have X^(2^i) mod (F-U) = X^(2^i). The first term of degree >= HFEDeg is X^(2^(HFEDegI+1)):
            2^(HFEDegI+1) >= HFEDeg but 2^HFEDegI < HFEDeg. So, we begin at the step i=HFEDegI+1 */
        /* Compute X^(2^(HFEDegI+1)) mod (F-U) */
        /* Step 1: compute X^(2^(HFEDegI+1)) */
        d = 2 << HFEDegI;
        /* Xqn is initialized to 0 with calloc, so the multiprecision word is initialized to 1 just by setting the first word */
        Xqn.set(d * NB_WORD_GFqn, 1);
        /* Step 2: reduction of X^(2^(HFEDegI+1)) modulo (F-U) */
        divsqr_r_HFE_cstdeg_gf2nx(Xqn, d, F, cst);
        i = HFEDegI + 1;
        for (; i < HFEn; ++i)
        {
            /* Step 1: (X^(2^i) mod (F-U))^2 = X^(2^(i+1)) */
            sqr_HFE_gf2nx(Xqn);
            /* Step 2: X^(2^(i+1)) mod (F-U) */
            divsqr_r_HFE_cstdeg_gf2nx(Xqn, (HFEDeg - 1) << 1, F, cst);
        }
        /* (X^(2^n) mod (F-U)) - X */
        Xqn.setXor(NB_WORD_GFqn, 1);
        /* Search the degree of X^(2^n) - X mod (F-U) */
        for (i = HFEDeg - 1, d = 0, mask = 0L; i > 0; --i)
        {
            b = Xqn.isNot0_gf2n(i * NB_WORD_GFqn, NB_WORD_GFqn);
            mask |= b;
            /* We add 1 to d as soon as we exceed all left zero coefficients */
            d += mask;
        }
        return d;
    }

    /**
     * @return The degree of Xqn.
     * @brief Computation of (X^(2^n) - X) mod (F-U).
     * @param[out] Xqn Xqn = (X^(2^n) - X) mod (F.poly-U) in GF(2^n)[X].
     * @param[in] F   A HFE polynomial in GF(2^n)[X] stored with a sparse rep.
     * @param[in] U   An element of GF(2^n).
     * @remark Requires to allocate (2*HFEDeg-1)*NB_WORD_GFqn words for Xqn.
     * @remark Requirement: F is monic.
     * @remark Requirement: F.L must be initialized with initListDifferences_gf2nx.
     * @remark Constant-time implementation.
     */
    private int frobeniusMap_multisqr_HFE_gf2nx(Pointer Xqn, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer U)
    {
        Pointer cst = new Pointer(NB_WORD_GFqn);
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer Xqn_cp = new Pointer();
        Pointer table_cp = new Pointer();
        Pointer Xqn_sqr = new Pointer(HFEDeg * NB_WORD_GFqn);
        Pointer current_coef = new Pointer();
        long b, mask;
        int d, i, j, k;
        /* Constant of F-U */
        cst.setRangeFromXor(0, F.poly, 0, U, 0, NB_WORD_GFqn);
        /* Table of the X^(k*2^II) mod F. */
        Pointer table = new Pointer((KX * HFEDeg + POW_II) * NB_WORD_GFqn);
        precompute_table(table, F, cst);
        /* X^(2^(HFEDegI+II)) = X^( (2^HFEDegI) * (2^II)) */
        /* We take the polynomial from the table */
        table.move((((1 << HFEDegI) - KP) * HFEDeg) * NB_WORD_GFqn);
        Xqn.copyFrom(table, HFEDeg * NB_WORD_GFqn);
        table.move(-(((1 << HFEDegI) - KP) * HFEDeg) * NB_WORD_GFqn);
        for (i = 0; i < ((HFEn - HFEDegI - II) / II); ++i)
        {
            /* Step 1: Xqn^(2^II) with II squarings */
            /* Xqn_sqr is the list of the coefficients of Xqn at the power 2^II */
            /* j=0, first squaring */
            for (k = 0; k < HFEDeg; ++k)
            {
                sqr_gf2n(Xqn_sqr, k * NB_WORD_GFqn, Xqn, k * NB_WORD_GFqn);
            }
            /* The other squarings */
            for (j = 1; j < II; ++j)
            {
                for (k = 0; k < HFEDeg; ++k)
                {
                    sqr_gf2n(Xqn_sqr, k * NB_WORD_GFqn, Xqn_sqr, k * NB_WORD_GFqn);
                }
            }
            /* Step 2: Reduction of Xqn^(2^II) modulo F, by using the table.
           Multiplication of ((X^(k*2^II)) mod F) by the current coefficient. */
            /* j=KP, initialization of the new Xqn */
            current_coef.changeIndex(Xqn_sqr, KP * NB_WORD_GFqn);
            table_cp.changeIndex(table);
            Xqn_cp.changeIndex(Xqn);
            for (k = 0; k < HFEDeg; ++k)
            {
                mul_gf2n(Xqn_cp, 0, table_cp, 0, current_coef, 0);
                Xqn_cp.move(NB_WORD_GFqn);
                table_cp.move(NB_WORD_GFqn);
            }
            for (j = KP + 1; j < HFEDeg; ++j)
            {
                current_coef.move(NB_WORD_GFqn);
                Xqn_cp.changeIndex(Xqn);
                for (k = 0; k < HFEDeg; ++k)
                {
                    mul_gf2n(mul_coef, 0, table_cp, 0, current_coef, 0);
                    Xqn_cp.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                    Xqn_cp.move(NB_WORD_GFqn);
                    table_cp.move(NB_WORD_GFqn);
                }
            }
            /* The coefficients such as X^(k*2^II) mod F = X^(k*2^II). */
            for (j = 0; j < KP; ++j)
            {
                /* (X^j)^II */
                Xqn.setXorRange(j * POW_II * NB_WORD_GFqn, Xqn_sqr, j * NB_WORD_GFqn, NB_WORD_GFqn);
            }
        }
        if ((HFEn - HFEDegI) % II != 0)
        {
            for (i = 0; i < ((HFEn - HFEDegI) % II); ++i)
            {
                /* Step 1: (X^(2^i) mod (F-U))^2 = X^(2^(i+1)) */
                sqr_HFE_gf2nx(Xqn);
                /* Step 2: X^(2^(i+1)) mod (F-U) */
                divsqr_r_HFE_cst_gf2nx(Xqn, F, cst);
            }
        }
        /* X^(2^n) - X */
        Xqn.setXor(NB_WORD_GFqn, 1L);
        for (i = HFEDeg - 1, d = 0, mask = 0L; i > 0; --i)
        {
            b = Xqn.isNot0_gf2n(i * NB_WORD_GFqn, NB_WORD_GFqn);
            mask |= b;
            /* We add 1 to d as soon as we exceed all left zero coefficients */
            d += mask;
        }
        return d;
    }

    /**
     * @return The degree of Xqn.
     * @brief Table of the X^(k*2^II) mod F, for Ceil(D/2^II) <= k < D.
     * @details We do not store X^(k*2^II) mod F when k*2^II < D, since
     * X^(k*2^II) mod F = X^(k*2^II) is already reduced.
     * @param[out] table   A vector of KX (D-1)-degree polynomials in GF(2^n)[X].
     * @param[in] F   A HFE polynomial in GF(2^n)[X] stored with a sparse rep.
     * @param[in] cst The constant of F, an element of GF(2^n).
     * @remark Requires to allocate (KX*HFEDeg+POW_II)*NB_WORD_GFqn words for table.
     * @remark Requirement: F is monic.
     * @remark Requirement: F.L must be initialized with initListDifferences_gf2nx.
     * @remark Constant-time implementation.
     */
    private void precompute_table(Pointer table, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer cst)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef;
        Pointer table_cp;
        int k, j, i;
        int table_orig = table.getIndex();
        /* First element of the table: X^(KP*(2^II)) mod F. */
        /* First step: X^(KP*(2^II)) mod F = X^(KP*(2^II)-D)*(F - X^D) mod F. */
        /* The first polynomial is initialized to 0. */
        for (i = 0; i < (HFEDeg + POW_II); ++i)
        {
            table.setRangeClear(i * NB_WORD_GFqn, NB_WORD_GFqn);
        }
        /* j=POW_II*KP-D, we reduce X^(D+j) mod F. */
        j = POW_II * KP - HFEDeg;
        /* i=0: constant of F */
        table_cp = new Pointer(table, NB_WORD_GFqn * j);
        table_cp.copyFrom(cst, NB_WORD_GFqn);
        for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
        {
            table_cp.move((int)F.L.get(i));
            table_cp.copyFrom(0, F.poly, i * NB_WORD_GFqn, NB_WORD_GFqn);
        }
        /* Second step: we compute X^(KP*(2^II)-D)*(F - X^D) mod F */
        /* We reduce one by one the coefficients leading_coef*X^(D+j) mod F,
       by using X^(D+j) = X^j * X^D = X^j * (F-X^D) mod F. */
        leading_coef = new Pointer(table, (j - 1 + HFEDeg) * NB_WORD_GFqn);
        for (--j; j != -1; --j)
        {
            /* i=0: constant of F */
            table_cp.changeIndex(table, NB_WORD_GFqn * j);
            mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
            table_cp.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
            {
                table_cp.move((int)F.L.get(i));
                mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                table_cp.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            }
            leading_coef.move(-NB_WORD_GFqn);
        }
        /* Computation of the other elements of the table: X^(k*(2^II)) mod F.
        X^(k*(2^II)) = (X^((k-1)*(2^II)) mod F) * X^(2^II) mod F. */
        for (k = KP + 1; k < HFEDeg; ++k)
        {
            /* Update the current polynomial */
            table_cp.changeIndex(table, HFEDeg * NB_WORD_GFqn);
            /* Multiplication of (X^((k-1)*(2^II)) mod F) by X^(2^II) */
            table_cp.setRangeClear(0, POW_II * NB_WORD_GFqn);
            table_cp.copyFrom(POW_II * NB_WORD_GFqn, table, 0, HFEDeg * NB_WORD_GFqn);
            /* Update the current polynomial */
            table.changeIndex(table_cp);
            /* Reduction of (X^((k-1)*(2^II)) mod F) * X^(2^II) modulo F */
            /* We reduce one by one the coefficients leading_coef*X^(D+j) mod F,
           by using X^(D+j) = X^j * X^D = X^j * (F-X^D) mod F. */
            leading_coef.changeIndex(table, (POW_II - 1 + HFEDeg) * NB_WORD_GFqn);
            for (j = POW_II - 1; j != -1; --j)
            {
                /* i=0: constant of F */
                table_cp.changeIndex(table, NB_WORD_GFqn * j);
                mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
                table_cp.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
                {
                    table_cp.move((int)F.L.get(i));
                    mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                    table_cp.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                }
                leading_coef.move(-NB_WORD_GFqn);
            }
        }
        table.changeIndex(table_orig);
    }

    private void divsqr_r_HFE_cst_gf2nx(Pointer poly, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer cst)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef = new Pointer();
        Pointer res = new Pointer();
        int i, dp;
        Pointer L = new Pointer(F.L);
        if (ENABLED_REMOVE_ODD_DEGREE)
        {
            for (dp = (HFEDeg - 1) << 1; dp > (HFEDeg + HFE_odd_degree); dp -= 2)
            {
                leading_coef.changeIndex(poly, dp * NB_WORD_GFqn);
                res.changeIndex(leading_coef, -HFEDeg * NB_WORD_GFqn);
                /* i=0: Constant of F-U */
                mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
                {
                    mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                    res.move((int)L.get(i));
                    res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                }
            }
            /* Here, dp=HFEDeg+HFE_odd_degree-1 */
            for (; dp >= HFEDeg; --dp)
            {
                /* i=0: Constant of F-U */
                leading_coef.changeIndex(poly, dp * NB_WORD_GFqn);
                res.changeIndex(leading_coef, -HFEDeg * NB_WORD_GFqn);
                /* i=0: Constant of F-U */
                mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
                {
                    mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                    res.move((int)L.get(i));
                    res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                }
            }
        }
        else
        {
            //div_r_HFE_cst_gf2nx
            for (dp = (HFEDeg - 1) << 1; dp >= HFEDeg; --dp)
            {
                leading_coef.changeIndex(poly, dp * NB_WORD_GFqn);
                res.changeIndex(leading_coef, -HFEDeg * NB_WORD_GFqn);
                /* i=0: Constant of F-U */
                mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
                {
                    mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                    res.move((int)L.get(i));
                    res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                }
            }
        }
    }

    private void divsqr_r_HFE_cstdeg_gf2nx(Pointer poly, int dp, SecretKeyHFE.complete_sparse_monic_gf2nx F, Pointer cst)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef = new Pointer();
        Pointer res = new Pointer();
        int i;
        Pointer L = new Pointer(F.L);
        for (; dp >= HFEDeg; --dp)
        {
            leading_coef.changeIndex(poly, dp * NB_WORD_GFqn);
            res.changeIndex(leading_coef, -HFEDeg * NB_WORD_GFqn);
            /* i=0: Constant of F-U */
            mul_gf2n(mul_coef, 0, leading_coef, 0, cst, 0);
            res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            for (i = 1; i < NB_COEFS_HFEPOLY; ++i)
            {
                mul_gf2n(mul_coef, 0, leading_coef, 0, F.poly, i * NB_WORD_GFqn);
                res.move((int)L.get(i));
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            }
        }
    }

    private void sqr_HFE_gf2nx(Pointer poly)
    {
        int i = NB_WORD_GFqn * (HFEDeg - 1);
        /* Pointer on the last coefficient of poly */
        poly.move(i);
        /* Pointer on the last coefficient of the square of poly */
        Pointer poly_2i = new Pointer(poly, i);
        /* Square of each coefficient, a_i X^i becomes a_i^2 X^(2i).
       Order: X^d X^(d-1) X^(d-2) ... X^(d-i) ... X^2 X^1 for d=HFEDeg-1 */
        for (i = 0; i < (HFEDeg - 1); ++i)
        {
            sqr_gf2n(poly_2i, 0, poly, 0);
            poly.move(-NB_WORD_GFqn);
            poly_2i.move(-NB_WORD_GFqn);
            /* The coefficient of X^(2(d-i)-1) is set to 0 (odd exponent) */
            poly_2i.setRangeClear(0, NB_WORD_GFqn);
            poly_2i.move(-NB_WORD_GFqn);
        }
        /* Square of the coefficient of X^0 */
        sqr_gf2n(poly, 0, poly, 0);
    }

    private void convHFEpolynomialSparseToDense_gf2nx(Pointer F_dense, SecretKeyHFE.complete_sparse_monic_gf2nx F)
    {
        Pointer F_cp = new Pointer(F.poly);
        int F_dense_orig = F_dense.getIndex();
        /* i=0: constant of F */
        F_dense.copyFrom(F_cp, NB_WORD_GFqn);
        for (int i = 1; i < NB_COEFS_HFEPOLY; ++i)
        {
            F_dense.move((int)F.L.get(i));
            F_dense.copyFrom(0, F_cp, i * NB_WORD_GFqn, NB_WORD_GFqn);
        }
        F_dense.changeIndex(F_dense_orig);
        /* Leading term: 1 */
        F_dense.set(HFEDeg * NB_WORD_GFqn, 1);
    }

    int div_r_gf2nx(Pointer A, int da, Pointer B, int db)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef = new Pointer(NB_WORD_GFqn);
        Pointer inv = new Pointer(NB_WORD_GFqn);
        Pointer res = new Pointer(A);
        int i;
        /* Compute the inverse of the leading term of B */
        inv_gf2n(inv, B, db * NB_WORD_GFqn);
        /* modular reduction */
        while (da >= db)
        {
            /* Search the current degree of A */
            while (A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0 && (da >= db))
            {
                --da;
            }
            if (da < db)
            {
                /* The computation of the remainder is finished */
                break;
            }
            res.changeIndex((da - db) * NB_WORD_GFqn);
            mul_gf2n(leading_coef, 0, A, da * NB_WORD_GFqn, inv, 0);
            /* i=0: Constant of B */
            mul_gf2n(mul_coef, 0, leading_coef, 0, B, 0);
            res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            for (i = 1; i < db; ++i)
            {
                mul_gf2n(mul_coef, 0, leading_coef, 0, B, i * NB_WORD_GFqn);
                res.move(NB_WORD_GFqn);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            }
            /* The leading term becomes 0 */
            /* useless because every coefficients >= db will be never used */
            /* set0_gf2n(leading_coef); */
            --da;
        }
        /* Here, da=db-1 */
        while (A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0 && da != 0)
        {
            --da;
        }
        /* Degree of the remainder */
        return da;
    }

    private void div_q_monic_gf2nx(Pointer A, int da, Pointer B, int db)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef, res;
        int i;
        /* modular reduction */
        while (da >= db)
        {
            /* Search the current degree of A */
            while (A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0 && da >= db)
            {
                --da;
            }
            if (da < db)
            {
                /* The computation of the remainder is finished */
                break;
            }
            leading_coef = new Pointer(A, da * NB_WORD_GFqn);
            i = (db << 1) - da;
            i = Math.max(0, i);
            res = new Pointer(A, (da - db + i) * NB_WORD_GFqn);
            for (; i < db; ++i)
            {
                mul_gf2n(mul_coef, 0, leading_coef, 0, B, i * NB_WORD_GFqn);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                res.move(NB_WORD_GFqn);
            }
            /* The leading term of A is a term of the quotient */
            --da;
        }
        if (da == -1)
        {
            ++da;
        }
        /* Here, da=db-1 */
        while (da != 0 && A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0)
        {
            --da;
        }
        /* Degree of the remainder */
    }

    private int div_r_monic_gf2nx(Pointer A, int da, Pointer B, int db)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer leading_coef, res;
        int i;
        /* modular reduction */
        while (da >= db)
        {
            /* Search the current degree of A */
            while (A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0 && da >= db)
            {
                --da;
            }
            if (da < db)
            {
                /* The computation of the remainder is finished */
                break;
            }
            leading_coef = new Pointer(A, da * NB_WORD_GFqn);
            res = new Pointer(leading_coef, -db * NB_WORD_GFqn);
            /* i=0: Constant of B */
            mul_gf2n(mul_coef, 0, leading_coef, 0, B, 0);
            res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            for (i = 1; i < db; ++i)
            {
                mul_gf2n(mul_coef, 0, leading_coef, 0, B, i * NB_WORD_GFqn);
                res.move(NB_WORD_GFqn);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
            }
            /* The leading term of A is a term of the quotient */
            --da;
        }
        if (da == (-1))
        {
            ++da;
        }
        /* Here, da=db-1 */
        while (da != 0 && A.is0_gf2n(da * NB_WORD_GFqn, NB_WORD_GFqn) != 0)
        {
            --da;
        }
        /* Degree of the remainder */
        return da;
    }

    private void inv_gf2n(Pointer res, Pointer A, int AOff)
    {
        int A_orig = A.getIndex();
        A.move(AOff);
        Pointer multi_sqr = new Pointer(NB_WORD_GFqn);
        int nb_sqr, i, j;
        /* i=pos */
        res.copyFrom(A, NB_WORD_GFqn);
        for (i = HFEn_1rightmost - 1; i != (-1); --i)
        {
            nb_sqr = (HFEn - 1) >>> (i + 1);
            /* j=0 */
            sqr_gf2n(multi_sqr, 0, res, 0);
            for (j = 1; j < nb_sqr; ++j)
            {
                sqr_gf2n(multi_sqr, 0, multi_sqr, 0);
            }
            mul_gf2n(res, 0, res, 0, multi_sqr, 0);
            if ((((HFEn - 1) >>> i) & 1) != 0)
            {
                sqr_gf2n(multi_sqr, 0, res, 0);
                mul_gf2n(res, 0, A, 0, multi_sqr, 0);
            }
        }
        sqr_gf2n(res, 0, res, 0);
        A.changeIndex(A_orig);
    }

    private void convMonic_gf2nx(Pointer F, int d)
    {
        Pointer inv = new Pointer(NB_WORD_GFqn);
        int F_orig = F.getIndex();
        F.move(d * NB_WORD_GFqn);
        /* At this step, F is the pointer on the term X^d of F */
        inv_gf2n(inv, F, 0);
        F.set1_gf2n(0, NB_WORD_GFqn);
        for (int i = d - 1; i != -1; --i)
        {
            F.move(-NB_WORD_GFqn);
            /* At this step, F is the pointer on the term X^i of F */
            mul_gf2n(F, 0, F, 0, inv, 0);
        }
        F.changeIndex(F_orig);
    }

    private void findRootsSplit_gf2nx(Pointer roots, Pointer f, int deg)
    {
        Pointer tmp_p = new Pointer();
        Pointer inv = new Pointer(NB_WORD_GFqn);
        int b, i, l, d;
        if (deg == 1)
        {
            /* Extract the unique root which is the constant of f */
            roots.copyFrom(f, NB_WORD_GFqn);
            return;
        }
        if ((HFEn & 1) != 0 && deg == 2)
        {
            findRootsSplit2_HT_gf2nx(roots, f);
            return;
        }
        Pointer poly_frob = new Pointer(((deg << 1) - 1) * NB_WORD_GFqn);
        /* poly_trace is modulo f, this degree is strictly less than deg */
        Pointer poly_trace = new Pointer(deg * NB_WORD_GFqn);
        /* f_cp a copy of f */
        Pointer f_cp = new Pointer((deg + 1) * NB_WORD_GFqn);
        do
        {
            /* Set poly_frob to zero */
            poly_frob.setRangeClear(0, ((deg << 1) - 1) * NB_WORD_GFqn);
            /* Set poly_trace to zero */
            poly_trace.setRangeClear(0, deg * NB_WORD_GFqn);
            /* Initialization to rX */
            /* Probability 2^(-n) to find 0 with a correct RNG */
            do
            {
                poly_trace.fillRandom(NB_WORD_GFqn, random, NB_BYTES_GFqn);
                /* Clean the last word (included the zero padding) */
                poly_trace.setAnd((NB_WORD_GFqn << 1) - 1, MASK_GF2n);
            }
            while (poly_trace.is0_gf2n(NB_WORD_GFqn, NB_WORD_GFqn) != 0);
            /* copy of f because the gcd modifies f */
            f_cp.copyFrom(f, (deg + 1) * NB_WORD_GFqn);
            traceMap_gf2nx(poly_trace, poly_frob, f_cp, deg);
            /* Degree of poly_trace */
            d = deg - 1;
            while (poly_trace.is0_gf2n(d * NB_WORD_GFqn, NB_WORD_GFqn) != 0 && d != 0)
            {
                --d;
            }
            l = gcd_gf2nx(f_cp, deg, poly_trace, d);
            b = buffer;
        }
        while ((l == 0) || (l == deg));
        if (b != 0)
        {
            tmp_p.changeIndex(poly_trace);
            poly_trace.changeIndex(f_cp);
            f_cp.changeIndex(tmp_p);
        }
        /* Here, f_cp is a non-trivial divisor of degree l */
        /* f_cp is the gcd */
        /* Here, it becomes monic */
        inv_gf2n(inv, f_cp, l * NB_WORD_GFqn);
        f_cp.set1_gf2n(l * NB_WORD_GFqn, NB_WORD_GFqn);
        for (i = l - 1; i != -1; --i)
        {
            mul_gf2n(f_cp, i * NB_WORD_GFqn, f_cp, i * NB_WORD_GFqn, inv, 0);
        }
        /* f = f_cp * Q */
        /* This function destroyes f */
        div_q_monic_gf2nx(f, deg, f_cp, l);
        /* Necessarily, the polynomial f is null here */
        /* f_cp is monic */
        /* We can apply findRootsSplit_gf2nx recursively */
        findRootsSplit_gf2nx(roots, f_cp, l);
        /* f is monic and f_cp is monic so Q is monic */
        /* We can apply findRootsSplit_gf2nx recursively */
        findRootsSplit_gf2nx(new Pointer(roots, l * NB_WORD_GFqn), new Pointer(f, l * NB_WORD_GFqn), deg - l);
    }

    void findRootsSplit2_HT_gf2nx(Pointer roots_orig, Pointer f_orig)
    {
        Pointer c = new Pointer(NB_WORD_GFqn);
        Pointer alpha = new Pointer(NB_WORD_GFqn);
        Pointer f = new Pointer(f_orig);
        //TODO: Since roots_orig is a Pointer32, the set and get method may have some bugs
        Pointer roots = new Pointer(roots_orig);
        sqr_gf2n(c, 0, f, NB_WORD_GFqn);
        inv_gf2n(roots, c, 0);
        mul_gf2n(c, 0, f, 0, roots, 0);
        findRootsSplit_x2_x_c_HT_gf2nx(alpha, c);
        f.move(NB_WORD_GFqn);
        mul_gf2n(roots, 0, alpha, 0, f, 0);
        roots.setRangeFromXor(NB_WORD_GFqn, roots, 0, f, 0, NB_WORD_GFqn);
    }

    void findRootsSplit_x2_x_c_HT_gf2nx(Pointer root, Pointer c)
    {
        Pointer alpha = new Pointer(NB_WORD_GFqn);
        final int e = (HFEn + 1) >>> 1;
        int i, j, e2, pos;
        /* Search the position of the MSB of n-1 */
        pos = 31;
        while ((e >>> pos) == 0)
        {
            --pos;
        }
        /* i=pos */
        root.copyFrom(c, NB_WORD_GFqn);
        for (i = pos - 1, e2 = 1; i != -1; --i)
        {
            e2 <<= 1;
            /* j=0 */
            sqr_gf2n(alpha, 0, root, 0);
            for (j = 1; j < e2; ++j)
            {
                sqr_gf2n(alpha, 0, alpha, 0);
            }
            root.setXorRange(0, alpha, 0, NB_WORD_GFqn);
            e2 = e >>> i;
            if ((e2 & 1) != 0)
            {
                sqr_gf2n(alpha, 0, root, 0);
                sqr_gf2n(root, 0, alpha, 0);
                root.setXorRange(0, c, 0, NB_WORD_GFqn);
            }
        }
    }

    private void traceMap_gf2nx(Pointer poly_trace, Pointer poly_frob, Pointer f, int deg)
    {
        int i = 1;
        /* (2^i) < deg does not require modular reduction by f */
        while ((1 << i) < deg)
        {
            /* poly_trace += ((rX)^(2^i)) mod f.  Here, ((rX)^(2^i)) mod f == (rX)^(2^i) since (2^i) < deg */
            sqr_gf2n(poly_trace, NB_WORD_GFqn << i, poly_trace, NB_WORD_GFqn << (i - 1));
            ++i;
        }
        /* Here, (rX)^(2^i) is the first time where we need modular reduction */
        if (i < HFEn)
        {
            /* poly_frob = (rX)^(2^i) = ((rX)^(2^(i-1)))^2 */
            sqr_gf2n(poly_frob, NB_WORD_GFqn << i, poly_trace, NB_WORD_GFqn << (i - 1));
            /* poly_frob = ((rX)^(2^i)) mod f */
            div_r_monic_cst_gf2nx(poly_frob, 1 << i, f, deg);
            /* poly_trace += ((rX)^(2^i)) mod f */
            poly_trace.setXorRange(0, poly_frob, 0, deg * NB_WORD_GFqn);
            for (++i; i < HFEn; ++i)
            {
                /* poly_frob = (rX)^(2^i) = ((rX)^(2^(i-1)) mod f)^2 */
                sqr_gf2nx(poly_frob, deg - 1);
                /* poly_frob = ((rX)^(2^i)) mod f */
                div_r_monic_cst_gf2nx(poly_frob, (deg - 1) << 1, f, deg);
                /* poly_trace += ((rX)^(2^i)) mod f */
                poly_trace.setXorRange(0, poly_frob, 0, deg * NB_WORD_GFqn);
            }
        }
    }

    private void div_r_monic_cst_gf2nx(Pointer A, int da, Pointer B, int db)
    {
        Pointer mul_coef = new Pointer(NB_WORD_GFqn);
        Pointer res;
        int i;
        int A_orig = A.getIndex();
        /* Pointer on the current leading term of A */
        A.move(da * NB_WORD_GFqn);
        for (; da >= db; --da)
        {
            res = new Pointer(A, -db * NB_WORD_GFqn);
            for (i = 0; i < db; ++i)
            {
                mul_gf2n(mul_coef, 0, A, 0, B, i * NB_WORD_GFqn);
                res.setXorRange(0, mul_coef, 0, NB_WORD_GFqn);
                res.move(NB_WORD_GFqn);
            }
            /* useless because every coefficients >= db will be never used */
            /* set0_gf2n(leading_coef); */
            A.move(-NB_WORD_GFqn);
        }
        A.changeIndex(A_orig);
    }

    private void sqr_gf2nx(Pointer poly, int d)
    {
        int i = NB_WORD_GFqn * d;
        /* Pointer on the last coefficient of poly */
        int poly_orig = poly.getIndex();
        poly.move(i);
        /* A pointer on X^(2*(d-i)) */
        /* Pointer on the last coefficient of the square of poly */
        Pointer poly_2i = new Pointer(poly, i);
        /* Square of each coefficient, a_i X^i becomes a_i^2 X^(2i). Order: X^d X^(d-1) X^(d-2) ... X^(d-i) ... X^2 X^1 */
        for (i = 0; i < d; ++i)
        {
            sqr_gf2n(poly_2i, 0, poly, 0);
            poly.move(-NB_WORD_GFqn);
            poly_2i.move(-NB_WORD_GFqn);
            /* The coefficient of X^(2(d-i)-1) is set to 0 (odd exponent) */
            poly_2i.setRangeClear(0, NB_WORD_GFqn);
            poly_2i.move(-NB_WORD_GFqn);
        }
        /* Square of the coefficient of X^0 */
        sqr_gf2n(poly, 0, poly, 0);
        poly.changeIndex(poly_orig);
    }

    /**
     * @brief Sort in ascending order of a vector in GF(2^n), in-place.
     * @details The fastest constant-time sort of this library.
     * The elements of GF(2^n) are seen as unsigned integers.
     * @param[in,out] tab A vector of l elements of GF(2^n). Will be sorted.
     * @param[in] l   The length of tab.
     * @remark Requirement: l>1.
     * @remark Constant-time implementation when l is not secret.
     */
    void fast_sort_gf2n(Pointer tab, int l)
    {
        Pointer tmp = new Pointer(NB_WORD_GFqn);
        Pointer prod = new Pointer(NB_WORD_GFqn);
        Pointer tab_i = new Pointer();
        Pointer tab_ipa = new Pointer();
        /* pow2_prev,pa,pb,pc are powers of two */
        int i, j, quo, rem, pow2_prev, pa, pb, pc;
        /* The power of 2 before l, which is 1<<position(MSB(l-1)). */
        pow2_prev = 2;
        while (pow2_prev < l)
        {
            pow2_prev <<= 1;
        }
        pow2_prev >>>= 1;
        for (pa = pow2_prev; pa > 1; pa >>>= 1)
        {
            /* Number of complete blocks */
            quo = l / (pa << 1);
            /* Size of the remainder block */
            rem = l - (pa << 1) * quo;
            /* Impact on the sort */
            rem = (rem <= pa) ? 0 : (rem - pa);
            tab_i.changeIndex(tab);
            tab_ipa.changeIndex(tab, pa * NB_WORD_GFqn);
            for (i = 0; i < quo; ++i)
            {
                for (j = 0; j < pa; ++j)
                {
                    CMP_AND_SWAP_CST_TIME(tab_i, tab_ipa, prod);
                    tab_i.move(NB_WORD_GFqn);
                    tab_ipa.move(NB_WORD_GFqn);
                }
                tab_i.move(pa * NB_WORD_GFqn);
                tab_ipa.move(pa * NB_WORD_GFqn);
            }
            for (j = 0; j < rem; ++j)
            {
                CMP_AND_SWAP_CST_TIME(tab_i, tab_ipa, prod);
                tab_i.move(NB_WORD_GFqn);
                tab_ipa.move(NB_WORD_GFqn);
            }
            i = 0;
            for (pb = pow2_prev; pb > pa; pb >>>= 1)
            {
                /* l>1 implies pb<l. */
                for (; i < (l - pb); ++i)
                {
                    if ((i & pa) == 0)
                    {
                        tab_ipa.changeIndex(tab, (i + pa) * NB_WORD_GFqn);
                        tmp.copyFrom(tab_ipa, NB_WORD_GFqn);
                        for (pc = pb; pc > pa; pc >>>= 1)
                        {
                            tab_i.changeIndex(tab, (i + pc) * NB_WORD_GFqn);
                            CMP_AND_SWAP_CST_TIME(tmp, tab_i, prod);
                        }
                        tab_ipa.copyFrom(tmp, NB_WORD_GFqn);
                    }
                }
            }
        }
        /* pa=1 */
        tab_i.changeIndex(tab);
        tab_ipa.changeIndex(tab, NB_WORD_GFqn);
        for (i = 0; i < (l - 1); i += 2)
        {
            CMP_AND_SWAP_CST_TIME(tab_i, tab_ipa, prod);
            tab_i.move(NB_WORD_GFqn << 1);
            tab_ipa.move(NB_WORD_GFqn << 1);
        }
        i = 0;
        tab_ipa.changeIndex(tab, NB_WORD_GFqn);
        for (pb = pow2_prev; pb > 1; pb >>>= 1)
        {
            /* l>1 implies pb<l. */
            for (; i < (l - pb); i += 2)
            {
                tmp.copyFrom(tab_ipa, NB_WORD_GFqn);
                for (pc = pb; pc > 1; pc >>>= 1)
                {
                    tab_i.changeIndex(tab, (i + pc) * NB_WORD_GFqn);
                    CMP_AND_SWAP_CST_TIME(tmp, tab_i, prod);
                }
                tab_ipa.copyFrom(tmp, NB_WORD_GFqn);
                tab_ipa.move(NB_WORD_GFqn << 1);
            }
        }
    }

    /**
     * @brief Sort in ascending order of a vector in GF(2^n), in-place.
     * @details Constant-time selection sort.
     * The elements of GF(2^n) are seen as unsigned integers.
     * @param[in,out] tab A vector of l elements of GF(2^n). Will be sorted.
     * @param[in] l   The length of tab.
     * @remark Constant-time implementation when l is not secret.
     * @remark Complexity: l(l-1)/2 steps.
     */
    private void selectionSort_gf2n(Pointer tab, int l)
    {
        int tab_orig = tab.getIndex();
        Pointer prod = new Pointer(NB_WORD_GFqn);
        Pointer tab_j = new Pointer(tab, NB_WORD_GFqn);
        int tab_lim = tab.getIndex() + NB_WORD_GFqn * (l - 1);
        for (; tab.getIndex() < tab_lim; tab.move(NB_WORD_GFqn))
        {
            for (tab_j.changeIndex(tab, NB_WORD_GFqn); tab_j.getIndex() <= tab_lim; tab_j.move(NB_WORD_GFqn))
            {
                CMP_AND_SWAP_CST_TIME(tab, tab_j, prod);
            }
        }
        tab.changeIndex(tab_orig);
    }

    private void CMP_AND_SWAP_CST_TIME(Pointer tab, Pointer tab_j, Pointer prod)
    {
        long mask = -cmp_lt_gf2n(tab_j, tab, NB_WORD_GFqn);
        Buffer_NB_WORD_GFqn.setRangeFromXor(0, tab, 0, tab_j, 0, NB_WORD_GFqn);
        prod.setRangeAndMask(0, Buffer_NB_WORD_GFqn, 0, NB_WORD_GFqn, mask);
        tab_j.setXorRange(0, prod, 0, NB_WORD_GFqn);
        tab.setXorRange(0, prod, 0, NB_WORD_GFqn);
    }

    private long cmp_lt_gf2n(Pointer a, Pointer b, int size)
    {
        long d, bo, mask;
        int i;
        /* Compute d the larger index such as a[d]!=b[d], in constant-time */
        d = 0;
        mask = 0;
        for (i = size - 1; i > 0; --i)
        {
            bo = a.get(i) ^ b.get(i);
            bo = GeMSSUtils.ORBITS_UINT(bo);
            mask |= bo;
            d += mask;
        }
        /* Return a[d]<b[d] in constant-time */
        mask = 0;
        for (i = 0; i < size; ++i)
        {
            bo = i ^ d;
            bo = GeMSSUtils.NORBITS_UINT(bo);
            mask |= (-bo) & GeMSSUtils.CMP_LT_UINT(a.get(i), b.get(i));
        }
        return mask;
    }

    public void compress_signHFE(byte[] sm8, Pointer sm)
    {
        byte[] sm64 = sm.toBytes(sm.getLength() << 3);
        int k2, sm64_cp = 0;
        /* Take the (n+v) first bits */
        System.arraycopy(sm64, 0, sm8, 0, NB_BYTES_GFqnv);
        /* Take the (Delta+v)*(nb_ite-1) bits */
        if (NB_ITE > 1)//(NB_ITE > 1) || HFEDELTA + HFEv == 0
        {
            int k1, nb_bits, nb_rem2, nb_rem_m, val_n;
            //if (HFEmr8)
            int nb_rem;
            //}
            /* HFEnv bits are already stored in sm8 */
            nb_bits = HFEnv;
            sm64_cp += (NB_WORD_GF2nv << 3) + (HFEmq8 & 7);
            for (k1 = 1; k1 < NB_ITE; ++k1)
            {
                /* Number of bits to complete the byte of sm8, in [0,7] */
                val_n = Math.min((HFEDELTA + HFEv), ((8 - (nb_bits & 7)) & 7));
                /* First byte of sm8 */
                if ((nb_bits & 7) != 0)
                {
                    if (HFEmr8 != 0)
                    {
                        sm8[nb_bits >>> 3] ^= ((sm64[sm64_cp] & 0xFF) >>> HFEmr8) << (nb_bits & 7);
                        /* Number of bits to complete the first byte of sm8 */
                        nb_rem = ((val_n - VAL_BITS_M));
                        if (nb_rem >= 0)
                        {
                            /* We take the next byte since we used VAL_BITS_M bits */
                            sm64_cp++;
                        }
                        if (nb_rem > 0)
                        {
                            nb_bits += VAL_BITS_M;
                            sm8[nb_bits >>> 3] ^= (sm64[sm64_cp] & 0xFF) << (nb_bits & 7);
                            nb_bits += nb_rem;
                        }
                        else
                        {
                            nb_bits += val_n;
                        }
                    }
                    else
                    {
                        /* We can take 8 bits, and we want at most 7 bits. */
                        sm8[nb_bits >>> 3] ^= (sm64[sm64_cp] & 0xFF) << (nb_bits & 7);
                        nb_bits += val_n;
                    }
                }
                /* Other bytes of sm8 */
                nb_rem2 = HFEDELTA + HFEv - val_n;
                /*nb_rem2 can be zero only in this case */
                /* Number of bits used of sm64, mod 8 */
                nb_rem_m = (HFEm + val_n) & 7;
                /* Other bytes */
                if (nb_rem_m != 0)
                {
                    /* -1 to take the ceil of /8, -1 */
                    for (k2 = 0; k2 < ((nb_rem2 - 1) >>> 3); ++k2)
                    {
                        sm8[nb_bits >>> 3] = (byte)(((sm64[sm64_cp] & 0xFF) >>> nb_rem_m) ^ ((sm64[sm64_cp + 1] & 0xFF) << (8 - nb_rem_m)));
                        nb_bits += 8;
                        sm64_cp++;
                    }
                    /* The last byte of sm8, between 1 and 8 bits to put */
                    sm8[nb_bits >>> 3] = (byte)((sm64[sm64_cp] & 0xFF) >>> nb_rem_m);
                    ++sm64_cp;
                    /* nb_rem2 between 1 and 8 bits */
                    nb_rem2 = ((nb_rem2 + 7) & 7) + 1;
                    if (nb_rem2 > (8 - nb_rem_m))
                    {
                        sm8[nb_bits >>> 3] ^= (byte)((sm64[sm64_cp] & 0xFF) << (8 - nb_rem_m));
                        ++sm64_cp;
                    }
                    nb_bits += nb_rem2;
                }
                else
                {
                    /* We are at the beginning of the bytes of sm8 and sm64 */
                    /* +7 to take the ceil of /8 */
                    for (k2 = 0; k2 < ((nb_rem2 + 7) >>> 3); ++k2)
                    {
                        sm8[nb_bits >>> 3] = sm64[sm64_cp];
                        nb_bits += 8;
                        ++sm64_cp;
                    }
                    /* The last byte has AT MOST 8 bits. */
                    nb_bits -= (8 - (nb_rem2 & 7)) & 7;
                }
                /* We complete the word. Then we search the first byte. */
                sm64_cp += ((8 - (NB_BYTES_GFqnv & 7)) & 7) + (HFEmq8 & 7);
            }
        }
    }

    void convMQS_one_to_last_mr8_equations_gf2(byte[] pk_U, PointerUnion pk_cp)
    {
        int ir, jq, jr;
        int pk_U_cp = 0;
        /* To have equivalence between *pk and pk[iq] */
        pk_cp.indexReset();
        pk_cp.moveNextBytes(HFEmq8);
        PointerUnion pk_cp2 = new PointerUnion(pk_cp);
        final int HFENq8 = NB_MONOMIAL_PK >>> 3;
        /* For each equation of result */
        for (ir = 0; ir < HFEmr8; ++ir)
        {
            /* Loop on every monomials */
            pk_cp2.changeIndex(pk_cp);
            for (jq = 0; jq < HFENq8; ++jq)
            {
                /* jr=0 */
                pk_U[pk_U_cp] = (byte)((pk_cp2.getByte() >>> ir) & 1);
                pk_cp2.moveNextBytes(NB_BYTES_GFqm);
                for (jr = 1; jr < 8; ++jr)
                {
                    pk_U[pk_U_cp] ^= (byte)((pk_cp2.getByte() >>> ir) & 1) << jr;
                    pk_cp2.moveNextBytes(NB_BYTES_GFqm);
                }
                ++pk_U_cp;
            }
            if (HFENr8 != 0)
            {
                /* jr=0 */
                pk_U[pk_U_cp] = (byte)((pk_cp2.getWithCheck() >>> ir) & 1);
                pk_cp2.moveNextBytes(NB_BYTES_GFqm);
                for (jr = 1; jr < HFENr8; ++jr)
                {
                    pk_U[pk_U_cp] ^= (byte)((pk_cp2.getWithCheck() >>> ir) & 1) << jr;
                    pk_cp2.moveNextBytes(NB_BYTES_GFqm);
                }
                ++pk_U_cp;
            }
        }
    }

    void convMQ_UL_gf2(byte[] pk, byte[] pk_U, int j)
    {
        int k, nb_bits, i, jj;
        int pk_p = ACCESS_last_equations8 + j * NB_BYTES_EQUATION;
        int pk_U_cp = j * NB_BYTES_EQUATION;
        /* Constant + x_0*x_0 */
        pk[pk_p] = (byte)(pk_U[pk_U_cp] & 3);
        Arrays.fill(pk, 1 + pk_p, NB_BYTES_EQUATION + pk_p, (byte)0);
        /* For each row of the output (the first is already done) */
        for (k = 2, i = 2; i <= HFEnv; ++i)
        {
            nb_bits = i;
            /* For each column */
            for (jj = HFEnv - 1; jj >= HFEnv - i; --jj, ++k)
            {
                pk[pk_p + (k >>> 3)] ^= ((pk_U[pk_U_cp + (nb_bits >>> 3)] >>> (nb_bits & 7)) & 1) << (k & 7);
                nb_bits += jj;
            }
        }
    }

    void convMQS_one_eq_to_hybrid_rep8_comp_gf2(byte[] pk, PointerUnion pk_cp)
    {
        byte[] pk_U = new byte[HFEmr8 * NB_BYTES_EQUATION];
        int i, j;
        convMQS_one_to_last_mr8_equations_gf2(pk_U, pk_cp);
        //convMQS_one_eq_to_hybrid_rep8_gf2(pk, pk_tmp)
        for (j = 0; j < HFEmr8; ++j)
        {
            convMQ_UL_gf2(pk, pk_U, j);
        }
        /* Monomial representation */
        pk_cp.indexReset();
        int pk_p = 0;
        for (i = 0; i < NB_MONOMIAL_PK; ++i)
        {
            for (j = 0; j < HFEmq8; ++j)
            {
                pk[pk_p] = pk_cp.getByte();
                pk_p++;
                pk_cp.moveNextByte();
            }
            /* Jump the coefficients of the HFEmr8 last equations */
            if (HFEmr8 != 0)
            {
                pk_cp.moveNextByte();
            }
        }
    }

    void convMQS_one_eq_to_hybrid_rep8_uncomp_gf2(byte[] pk, PointerUnion pk_cp)
    {
        byte[] pk_U = new byte[HFEmr8 * NB_BYTES_EQUATION];
        int i, j, k, nb_bits;
        long val = 0;
        convMQS_one_to_last_mr8_equations_gf2(pk_U, pk_cp);
        for (j = 0; j < HFEmr8 - 1; ++j)
        {
            convMQ_UL_gf2(pk, pk_U, j);
        }
        pk_cp.indexReset();
        /* The last equation is smaller because compressed */
        int pk2_cp = ACCESS_last_equations8 + j * NB_BYTES_EQUATION;
        int pk_U_cp = j * NB_BYTES_EQUATION;
        if (HFENr8 != 0 && (HFEmr8 > 1))
        {
            final int SIZE_LAST_EQUATION = ((NB_MONOMIAL_PK - ((HFEmr8 - 1) * HFENr8c) + 7) >>> 3);
            /* Constant + x_0*x_0 */
            pk[pk2_cp] = (byte)(pk_U[pk_U_cp] & 3);
            for (i = 1; i < SIZE_LAST_EQUATION; ++i)
            {
                pk[pk2_cp + i] = 0;
            }
            /* For each row of the output (the first is already done) */
            for (k = 2, i = 2; i < HFEnv; ++i)
            {
                nb_bits = i;
                /* For each column */
                for (j = HFEnv - 1; j >= HFEnv - i; --j, ++k)
                {
                    pk[pk2_cp + (k >>> 3)] ^= ((pk_U[pk_U_cp + (nb_bits >>> 3)] >>> (nb_bits & 7)) & 1) << (k & 7);
                    nb_bits += j;
                }
            }
            /* i == HFEnv */
            nb_bits = HFEnv;
            /* For each column */
            for (j = HFEnv - 1; j >= LOST_BITS; --j, ++k)
            {
                pk[pk2_cp + (k >>> 3)] ^= ((pk_U[pk_U_cp + (nb_bits >>> 3)] >>> (nb_bits & 7)) & 1) << (k & 7);
                nb_bits += j;
            }
            for (; j >= 0; --j, ++k)
            {
                val ^= ((long)((pk_U[pk_U_cp + (nb_bits >>> 3)] >>> (nb_bits & 7)) & 1)) << (LOST_BITS - 1 - j);
                nb_bits += j;
            }
        }
        /* We put the last bits (stored in val) and we put it in the zero padding of each equation (excepted in
        the last since it is not complete since we use its last bits to fill the paddings) */
        pk2_cp = ACCESS_last_equations8 - 1;
        for (j = 0; j < (HFEmr8 - 1); ++j)
        {
            /* Last byte of the equation */
            pk2_cp += NB_BYTES_EQUATION;
            pk[pk2_cp] ^= ((byte)(val >>> (j * HFENr8c))) << HFENr8;
        }
        /* Monomial representation */
        pk_cp.indexReset();
        int pk_p = 0;
        for (i = 0; i < NB_MONOMIAL_PK; ++i)
        {
            for (j = 0; j < HFEmq8; ++j)
            {
                pk[pk_p] = pk_cp.getByte();
                pk_p++;
                pk_cp.moveNextByte();
            }
            /* Jump the coefficients of the HFEmr8 last equations */
            if (HFEmr8 != 0)
            {
                pk_cp.moveNextByte();
            }
        }
    }

    public int crypto_sign_open(byte[] PK, byte[] message, byte[] signature)
    {
        Pointer pk_tmp = new Pointer(1 + NB_WORD_UNCOMP_EQ * HFEmr8);//if (HFEmr8 != 0)
        PointerUnion pk = new PointerUnion(PK);
        int i;
        long val = 0;
        if (HFENr8 != 0 && (HFEmr8 > 1))
        {
            PointerUnion pk_cp = new PointerUnion(pk);
            pk_cp.moveNextBytes(ACCESS_last_equations8 - 1);
            for (i = 0; i < HFEmr8 - 1; ++i)
            {
                /* Last byte of the equation */
                pk_cp.moveNextBytes(NB_BYTES_EQUATION);
                val ^= ((pk_cp.getByte() & 0xFFL) >>> HFENr8) << (i * HFENr8c);
            }
        }
        if (HFEmr8 != 0)
        {
            long cst = 0;
            PointerUnion pk64 = new PointerUnion(pk);
            for (i = 0; i < HFEmr8 - 1; i++)
            {
                pk64.setByteIndex(ACCESS_last_equations8 + i * NB_BYTES_EQUATION);
                cst ^= convMQ_uncompressL_gf2(new Pointer(pk_tmp, 1 + i * NB_WORD_UNCOMP_EQ), pk64) << i;
            }
            pk64.setByteIndex(ACCESS_last_equations8 + i * NB_BYTES_EQUATION);
            /* The last equation in input is smaller because compressed */
            cst ^= convMQ_last_uncompressL_gf2(new Pointer(pk_tmp, 1 + i * NB_WORD_UNCOMP_EQ), pk64) << i;
            if (HFENr8 != 0 && (HFEmr8 > 1))
            {
                /* Number of lost bits by the zero padding of each equation (without the last) */
                if (HFEnvr == 0)
                {
                    //redgemss128
                    pk_tmp.setXor((i + 1) * NB_WORD_UNCOMP_EQ, val << (64 - LOST_BITS));
                }
                else if (HFEnvr > LOST_BITS)
                {
                    //gemss192, bluegemss128, bluegemss192, redgemss192, redgemss256, whitegemss128, whitegemss256
                    //cyangemss128, cyangemss192, cyangemss256, magentagemss192
                    pk_tmp.setXor((i + 1) * NB_WORD_UNCOMP_EQ, val << (HFEnvr - LOST_BITS));
                }
                else if (HFEnvr == LOST_BITS)
                {
                    //gemss256, bluegemss256
                    pk_tmp.set((i + 1) * NB_WORD_UNCOMP_EQ, val);
                }
                else if (HFEnvr < LOST_BITS)
                {
                    // whitegemss192, magentagemss128, magentagemss256
                    pk_tmp.setXor((i + 1) * NB_WORD_UNCOMP_EQ - 1, val << (64 - (LOST_BITS - HFEnvr)));
                    pk_tmp.set((i + 1) * NB_WORD_UNCOMP_EQ, val >>> (LOST_BITS - HFEnvr));
                }
            }
            cst <<= HFEmr - HFEmr8;
            pk_tmp.set(cst);
        }
        int ret = 0;
        if (HFEmr8 != 0)
        {
            ret = sign_openHFE_huncomp_pk(message, message.length, signature, pk, new PointerUnion(pk_tmp));
        }
        return ret;
    }

    /**
     * @return 0 if the result is correct, ERROR_ALLOC for error from
     * malloc/calloc functions.
     * @brief Apply the change of variables x'=xS to a MQS stored with a monomial
     * representation.
     * @details MQS = (c,Q), with c the constant part in GF(2^n) and Q is an upper
     * triangular matrix of size (n+v)*(n+v) in GF(2^n). We have MQS = c + xQxt
     * with x =  (x0 x1 ... x_(n+v)). At the end of the function, we have
     * MQS = c + xQ'xt with Q' = SQSt. We multiply S by Q, then SQ by St.
     * @param[in,out] MQS A MQS in GF(2^n)[x1,...,x_(n+v)] (n equations,
     * n+v variables).
     * @param[in] S   A matrix (n+v)*(n+v) in GF(2). S should be invertible
     * (by definition of a change of variables).
     * @remark This function should be faster than changeVariablesMQS_simd_gf2
     * when SIMD is not used.
     * @remark Constant-time implementation.
     */
    void changeVariablesMQS64_gf2(Pointer MQS, Pointer S)
    {
        Pointer MQS2, MQS2_cp;
        long bit_kr;
        Pointer MQS_cpi, MQS_cpj = new Pointer();
        Pointer S_cpi, S_cpj;
        int iq, ir, j, jq, jr, kq;
        /* Tmp matrix (n+v)*(n+v) of quadratic terms to compute S*Q */
        MQS2 = new Pointer(HFEnv * HFEnv * NB_WORD_GFqn);
        /* To avoid the constant of MQS */
        MQS_cpi = new Pointer(MQS, NB_WORD_GFqn);
        MQS2_cp = new Pointer(MQS2);
        S_cpj = new Pointer(S);
        /* Step 1 : compute MQS2 = S*Q */
        /* Use multiplication by transpose (so by rows of Q) */
        /* It is possible because X*Q*tX = X*tQ*tX (with X = (x1 ... xn)) */
        /* Warning : Q is a upper triangular matrix in GF(q^n) */
        /* In this code, we have : */
        /* i = iq*NB_BITS_UINT + ir */
        /* k = kq*NB_BITS_UINT + kr */
        /* *MQS_cpi = MQS[NB_WORD_GFqn] */
        /* *MQS_cpj = MQS_cpi[(((i*(2n-i+1))/2) + k)*NB_WORD_GFqn] */
        /* The previous formula is a bit complicated, so the idea is :
         *MQS_cpj would equal MQS_cpi[i][i+k] if MQS used n*n in memory */
        /* *MQS2_cp = MQS2[i*NB_WORD_GFqn] */
        /* *S_cpj = S[j*NB_WORD_GFqn+iq] */
        /* for each row j of S */
        for (j = 0; j < HFEnv; ++j)
        {
            /* initialisation at the first row of Q */
            MQS_cpj.changeIndex(MQS_cpi);
            /* for each row of Q excepted the last block */
            for (iq = 0; iq < HFEnvq; ++iq)
            {
                for (ir = 0; ir < NB_BITS_UINT; ++ir)
                {
                    /* Compute a dot product */
                    bit_kr = S_cpj.get() >>> ir;
                    LOOPKR(MQS_cpj, MQS2_cp, bit_kr, ir, NB_BITS_UINT);
                    for (kq = 1; kq < (HFEnvq - iq); ++kq)
                    {
                        bit_kr = S_cpj.get(kq);
                        LOOPKR(MQS_cpj, MQS2_cp, bit_kr, 0, NB_BITS_UINT);
                    }
                    LOOPKR_REMAINDER(MQS2_cp, S_cpj, MQS_cpj, kq);
                    /* update the next element to compute */
                    MQS2_cp.move(NB_WORD_GFqn);
                }
                /* 64 bits of zero in Q */
                S_cpj.moveIncremental();
            }
            /* the last block */
            if (HFEnvr != 0)
            {
                for (ir = 0; ir < HFEnvr; ++ir)
                {
                    /* Compute a dot product */
                    bit_kr = S_cpj.get() >>> ir;
                    LOOPKR(MQS_cpj, MQS2_cp, bit_kr, ir, HFEnvr);
                    /* update the next element to compute */
                    MQS2_cp.move(NB_WORD_GFqn);
                }
                /* Next row of S */
                S_cpj.moveIncremental();
            }
        }
        /* Step 2 : compute MQS = MQS2*tS = (S*Q)*tS */
        /* Use multiplication by transpose (so by rows of S) */
        /* Permute MQS and MQS2 */
        MQS_cpi.changeIndex(MQS2);
        MQS2_cp.changeIndex(MQS, NB_WORD_GFqn);
        S_cpi = new Pointer(S);
        /* First : compute upper triangular result */
        /* In this code, we have : */
        /* *MQS_cpi = MQS2[j*n*NB_WORD_GFqn] */
        /* *MQS_cpj = MQS2[(j*n+k)*NB_WORD_GFqn] */
        /* *MQS2_cp = MQS[(((j*(2n-j+1))/2) + i-j)*NB_WORD_GFqn] */
        /* The previous formula is a bit complicated, so the idea is :
         *MQS2_cp would equal MQS[j][i] if MQS used n*n in memory */
        /* *S_cpi = S[j*NB_WORD_GFqn] */
        /* *S_cpj = S[i*NB_WORD_GFqn] */
        /* for each row j of MQS2 excepted the last block */
        for (jq = 0; jq < HFEnvq; ++jq)
        {
            for (jr = 0; jr < NB_BITS_UINT; ++jr)
            {
                S_cpj.changeIndex(S_cpi);
                /* for each row >=j of S */
                LOOPIR_INIT(MQS2_cp, MQS_cpj, MQS_cpi, S_cpj, jr, NB_BITS_UINT);
                for (iq = jq + 1; iq < HFEnvq; ++iq)
                {
                    LOOPIR_INIT(MQS2_cp, MQS_cpj, MQS_cpi, S_cpj, 0, NB_BITS_UINT);
                }
                /* the last block */
                if (HFEnvr != 0)
                {
                    LOOPIR_INIT(MQS2_cp, MQS_cpj, MQS_cpi, S_cpj, 0, HFEnvr);
                }
                /* Next row of MQS2 */
                MQS_cpi.changeIndex(MQS_cpj);
                /* Next row of S because of upper triangular */
                S_cpi.move(NB_WORD_GF2nv);
            }
        }
        /* the last block */
        if (HFEnvr != 0)
        {
            for (jr = 0; jr < HFEnvr; ++jr)
            {
                S_cpj.changeIndex(S_cpi);
                MQS_cpj.changeIndex(MQS_cpi);
                /* for each row >=j of S, the last block */
                LOOPIR_INIT(MQS2_cp, MQS_cpj, MQS_cpi, S_cpj, jr, HFEnvr);
                MQS_cpi.changeIndex(MQS_cpj);
                S_cpi.move(NB_WORD_GF2nv);
            }
        }
        /* Second : compute lower triangular result */
        MQS_cpi.changeIndex(MQS2);
        MQS2_cp.changeIndex(MQS, NB_WORD_GFqn);
        S_cpj.changeIndex(S);
        /* In this code, we have : */
        /* *MQS_cpi = MQS2[(j+1)*n*NB_WORD_GFqn] */
        /* *MQS_cpj = MQS2[(j+1)*n+k)*NB_WORD_GFqn] */
        /* *MQS2_cp = MQS[(((j*(2n-j+1))/2) + i-j)*NB_WORD_GFqn] */
        /* The previous formula is a bit complicated, so the idea is :
         *MQS2_cp would equal MQS[j][i] if MQS used n*n in memory */
        /* *S_cpj = S[j*NB_WORD_GFqn] */
        /* for each row j of S excepted the last block */
        for (jq = 0; jq < HFEnvq; ++jq)
        {
            for (jr = 0; jr < NB_BITS_UINT; ++jr)
            {
                /* i=j : the diagonal is already computing */
                MQS2_cp.move(NB_WORD_GFqn);
                /* The line j of MQS2 is useless */
                MQS_cpi.move(HFEnv * NB_WORD_GFqn);
                MQS_cpj.changeIndex(MQS_cpi);
                /* for each row >j of MQS2 */
                LOOPIR_LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj, jr + 1, NB_BITS_UINT);
                for (iq = jq + 1; iq < HFEnvq; ++iq)
                {
                    LOOPIR_LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj, 0, NB_BITS_UINT);
                }
                /* the last block */
                if (HFEnvr != 0)
                {
                    LOOPIR_LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj, 0, HFEnvr);
                }
                /* Next row of S */
                S_cpj.move(NB_WORD_GF2nv);
            }
        }
        /* the last block excepted the last row */
        if (HFEnvr != 0)
        {
            for (jr = 0; jr < HFEnvr - 1; ++jr)
            {
                /* i=j : the diagonal is already computing */
                MQS2_cp.move(NB_WORD_GFqn);
                /* The line j of MQS2 is useless */
                MQS_cpi.move(HFEnv * NB_WORD_GFqn);
                MQS_cpj.changeIndex(MQS_cpi);
                /* for each row >=j of S */
                /* the last block */
                LOOPIR_LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj, jr + 1, HFEnvr);
                /* Next row of S */
                S_cpj.move(NB_WORD_GF2nv);
            }
        }
        MQS.indexReset();
        S.indexReset();
    }

    private void LOOPIR_INIT(Pointer MQS2_cp, Pointer MQS_cpj, Pointer MQS_cpi, Pointer S_cpj, int STARTIR,
                             int NB_ITIR)
    {
        for (int ir = STARTIR; ir < NB_ITIR; ++ir)
        {
            MQS2_cp.setRangeClear(0, NB_WORD_GFqn);
            MQS_cpj.changeIndex(MQS_cpi);
            /* Compute a dot product */
            LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj);
            /* update the next element to compute */
            MQS2_cp.move(NB_WORD_GFqn);
            /* update the next row of S to use */
            S_cpj.move(NB_WORD_GF2nv);
        }
    }

    private void LOOPIR_LOOPK_COMPLETE(Pointer MQS2_cp, Pointer S_cpj, Pointer MQS_cpj, int STARTIR, int NB_ITIR)
    {
        for (int ir = STARTIR; ir < NB_ITIR; ++ir)
        {
            /* Compute a dot product */
            LOOPK_COMPLETE(MQS2_cp, S_cpj, MQS_cpj);
            /* update the next element to compute */
            MQS2_cp.move(NB_WORD_GFqn);
        }
    }

    private void LOOPK_COMPLETE(Pointer MQS2_cp, Pointer S_cpj, Pointer MQS_cpj)
    {
        long bit_kr;
        int kq;
        for (kq = 0; kq < HFEnvq; ++kq)
        {
            bit_kr = S_cpj.get(kq);
            LOOPKR(MQS_cpj, MQS2_cp, bit_kr, 0, NB_BITS_UINT);
        }
        LOOPKR_REMAINDER(MQS2_cp, S_cpj, MQS_cpj, kq);
    }

    private void LOOPKR(Pointer MQS_cpj, Pointer MQS2_cp, long bit_kr, int START, int NB_IT)
    {
        long mask;
        for (int kr = START; kr < NB_IT; ++kr)
        {
            /* multiply one bit of S by one element of MQS_cpj */
            mask = -(bit_kr & 1L);
            MQS2_cp.setXorRangeAndMask(0, MQS_cpj, 0, NB_WORD_GFqn, mask);
            MQS_cpj.move(NB_WORD_GFqn);
            bit_kr >>>= 1;
        }
    }

    private void LOOPKR_REMAINDER(Pointer MQS2_cp, Pointer S_cpj, Pointer MQS_cpj, int kq)
    {
        if (HFEnvr != 0)
        {
            LOOPKR(MQS_cpj, MQS2_cp, S_cpj.get(kq), 0, HFEnvr);
        }
    }

    /**
     * @return 0 if the result is correct, ERROR_ALLOC for error from
     * malloc/calloc functions.
     * @brief Computation of the multivariate representation of a HFEv
     * polynomial, then a change of variables is applied.
     * @details Computation of the multivariate representation of F(XS),
     * by evaluation/interpolation. We take the following N points in GF(2)^(n+v) :
     * n0=(0 ... 0),
     * e1,e2,...,e_(n+v) with ei the i-th row of the identity matrix,
     * all ei+ej, with i<j.
     * Let p be a MQS, we have:
     * p(n0) = cst,
     * p(ei) = cst + p_i,
     * p(ei+ej) = cst + p_i + p_j + p_i,j.
     * So, these N evaluations give directly p. The interpolation is trivial.
     * @param[in] F   A monic HFEv polynomial in GF(2^n)[X,x_(n+1),...,x_(n+v)]
     * stored with a sparse representation.
     * @param[in] S   A matrix (n+v)*(n+v) in GF(2). S should be invertible
     * (by definition of a change of variables).
     * @param[out] MQS A MQS in GF(2^n)[x1,...,x_(n+v)] (n equations,
     * n+v variables). MQS is stored as one equation in GF(2^n)[x1,...,x_(n+v)]
     * (monomial representation + quadratic form cst||Q).
     * @remark Requires to allocate MQnv_GFqn_SIZE words for MQS.
     * @remark Requirement: F is monic.
     * @remark Constant-time implementation.
     */
    int interpolateHFE_FS_ref(Pointer MQS, Pointer F, Pointer S)
    {
        Pointer e_ijS = new Pointer(NB_WORD_GF2nv);
        Pointer tab_eval, tab_eval_i, tab_eval_i2 = new Pointer();
        Pointer e_iS, e_i2S = new Pointer();
        int i, i2;
        /* Let e_i be the i-th row of the identity matrix */
        /* We compute all F(e_i*S), then all F((e_i+e_j)S) */
        /* Table of the F(e_i*S) */
        tab_eval = new Pointer(HFEnv * NB_WORD_GFqn);
        /* Constant: copy the first coefficient of F in MQS */
        MQS.copyFrom(F, NB_WORD_GFqn);
        /* e_i*S corresponds to the i-th row of S */
        e_iS = new Pointer(S);
        tab_eval_i = new Pointer(tab_eval);
        for (i = 0; i < HFEnv; ++i)
        {
            /* F(e_i*S) = cst + p_i */
            evalHFEv_gf2nx(tab_eval_i, F, e_iS);
            tab_eval_i.move(NB_WORD_GFqn);
            /* Next e_i */
            e_iS.move(NB_WORD_GF2nv);
        }
        e_iS.changeIndex(S);
        tab_eval_i.changeIndex(tab_eval);
        for (i = 0; i < HFEnv; ++i)
        {
            /* Update of MQS with F(e_i*S) from tab_eval */
            MQS.move(NB_WORD_GFqn);
            /* p_i = F(e_i*S) + cst */
            tab_eval_i.setXorRange(0, F, 0, NB_WORD_GFqn);
            MQS.copyFrom(tab_eval_i, NB_WORD_GFqn);
            /* Computation of p_i,i2 by computing F((e_i+e_i2)*S) */
            tab_eval_i2.changeIndex(tab_eval_i);
            e_i2S.changeIndex(e_iS);
            for (i2 = i + 1; i2 < HFEnv; ++i2)
            {
                MQS.move(NB_WORD_GFqn);
                tab_eval_i2.move(NB_WORD_GFqn);
                e_i2S.move(NB_WORD_GF2nv);
                /* F((e_i+e_i2)*S) = cst + p_i + p_i2 + p_i,i2 */
                /* F((e_i+e_i2)*S) */
                e_ijS.setRangeFromXor(0, e_iS, 0, e_i2S, 0, NB_WORD_GF2nv);
                evalHFEv_gf2nx(MQS, F, e_ijS);
                MQS.setXorRangeXor(0, tab_eval_i, 0, tab_eval_i2, 0, NB_WORD_GFqn);
//                /* + p_i */
//                MQS.setXorRange(0, tab_eval_i, 0, NB_WORD_GFqn);
//                /* + p_i2 + cst */
//                MQS.setXorRange(0, tab_eval_i2, 0, NB_WORD_GFqn);
            }
            tab_eval_i.move(NB_WORD_GFqn);
            e_iS.move(NB_WORD_GF2nv);
        }
        MQS.indexReset();
        return 0;
    }

    /**
     * @brief Evaluation of F in (X,v), with F a HFEv polynomial.
     * @details Firstly, we compute X^(q^j) for j=0 to HFEDegI.
     * Then, we compute, sum_j of X^(q^j)*(Bi + sum_k=0_to_(j-1) A_j,k X^(q^k)).
     * When D is a power of two, we add X^(D/2) to the last sum_k (to obtain the
     * monic term X^D). Each sum is computed in GF(2)[x], and the modular reduction
     * is computed at the end.
     * @param[out] Fxv The evaluation of F in xv, in GF(2^n).
     * @param[in] F   A monic HFEv polynomial in GF(2^n)[X] stored with a sparse
     * representation.
     * @param[in] xv  A vector of n+v elements in GF(2).
     * @remark Requirement: F is monic.
     * @remark Constant-time implementation.
     * @remark Complexity: (#F-2) multiplications in GF(2)[x],
     * Ceil(Log_2(D))+1 modular reductions,
     * Ceil(Log_2(D))-1 squares in GF(2^n).
     * We can compare to the complexity of the Horner method:
     * (#F-2) multiplications in GF(2^n),
     * Floor(Log_2(D))-2 squares in GF(2^n).
     */
    void evalHFEv_gf2nx(Pointer Fxv, Pointer F, Pointer xv)
    {
        Pointer cur_acc = new Pointer(NB_WORD_MUL);
        Pointer prod = new Pointer(NB_WORD_MUL);
        Pointer acc = new Pointer(NB_WORD_MUL);
        Pointer tab_Xqj = new Pointer((HFEDegI + 1) * NB_WORD_GFqn);
        Pointer tab_Xqj_cp2 = new Pointer();
        int j, k;
        int F_orig = F.getIndex();
        Pointer V = new Pointer(NB_WORD_GFqv);
        Pointer tab_Xqj_cp = new Pointer(tab_Xqj, NB_WORD_GFqn);
        /* j=0: X^(2^0) */
        tab_Xqj.copyFrom(xv, NB_WORD_GFqn);
        tab_Xqj.setAnd(NB_WORD_GFqn - 1, MASK_GF2n);
        /* Compute X^(2^j) */
        for (j = 1; j <= HFEDegI; ++j)
        {
            sqr_gf2n(tab_Xqj_cp, 0, tab_Xqj_cp, -NB_WORD_GFqn);
            tab_Xqj_cp.move(NB_WORD_GFqn);
        }
        /* Evaluation of the constant, quadratic in the vinegars */
        int endloop = (NB_WORD_GFqn + NB_WORD_GFqv) == NB_WORD_GF2nv ? NB_WORD_GFqv : NB_WORD_GFqv - 1;
        for (j = 0; j < endloop; ++j)
        {
            V.set(j, (xv.get(NB_WORD_GFqn - 1 + j) >>> HFEnr) ^ (xv.get(NB_WORD_GFqn + j) << (64 - HFEnr)));
        }
        if ((NB_WORD_GFqn + NB_WORD_GFqv) != NB_WORD_GF2nv)
        {
            V.set(j, xv.get(NB_WORD_GFqn - 1 + j) >>> HFEnr);
        }
        /* Evaluation of the vinegar constant */
        evalMQSv_unrolled_gf2(cur_acc, V, F);
        set0_high_product_gf2n(cur_acc);
        F.move(MQv_GFqn_SIZE);
        /* Evaluation of the linear terms in the vinegars */
        /* + evaluation of the linear and quadratic terms in X */
        /* j=0 */
        /* Degree 1 term */
        /* Linear term */
        vecMatProduct(acc, V, new Pointer(F, NB_WORD_GFqn), 0, FunctionParams.V);
        acc.setXorRange(0, F, 0, NB_WORD_GFqn);
        tab_Xqj_cp.changeIndex(tab_Xqj);
        /* mul by X */
        prod.mul_gf2x(tab_Xqj_cp, acc);
        cur_acc.setXorRange(0, prod, 0, NB_WORD_MMUL);
        F.move(MLv_GFqn_SIZE);
        /* X^(q^j) * (sum a_j,k X^q^k) */
        for (j = 1; j < HFEDegI; ++j)
        {
            /* Linear term */
            vecMatProduct(acc, V, new Pointer(F, NB_WORD_GFqn), 0, FunctionParams.V);
            acc.setXorRange(0, F, 0, NB_WORD_GFqn);
            set0_high_product_gf2n(acc);
            F.move(MLv_GFqn_SIZE);
            /* Quadratic terms */
            tab_Xqj_cp2.changeIndex(tab_Xqj_cp);
            for (k = 0; k < j; ++k)
            {
                prod.mul_gf2x(F, tab_Xqj_cp2);
                acc.setXorRange(0, prod, 0, NB_WORD_MMUL);
                F.move(NB_WORD_GFqn);
                tab_Xqj_cp2.move(NB_WORD_GFqn);
            }
            rem_gf2n(acc, 0, acc);
            prod.mul_gf2x(tab_Xqj_cp2, acc);
            cur_acc.setXorRange(0, prod, 0, NB_WORD_MMUL);
        }
        /* j=HFEDegI */
        vecMatProduct(acc, V, new Pointer(F, NB_WORD_GFqn), 0, FunctionParams.V);
        acc.setXorRange(0, F, 0, NB_WORD_GFqn);
        F.move(MLv_GFqn_SIZE);
        /* Quadratic terms */
        tab_Xqj_cp2.changeIndex(tab_Xqj_cp);
        if (HFEDegJ != 0)
        {
            set0_high_product_gf2n(acc);
            for (k = 0; k < HFEDegJ; ++k)
            {
                prod.mul_gf2x(F, tab_Xqj_cp2);
                acc.setXorRange(0, prod, 0, NB_WORD_MMUL);
                F.move(NB_WORD_GFqn);
                tab_Xqj_cp2.move(NB_WORD_GFqn);
            }
            /* k=HFEDegJ : monic case */
            acc.setXorRange(0, tab_Xqj_cp2, 0, NB_WORD_GFqn);
            rem_gf2n(acc, 0, acc);
        }
        else
        {
            /* k=HFEDegJ : monic case */
            acc.setRangeFromXor(0, acc, 0, tab_Xqj_cp2, 0, NB_WORD_GFqn);
        }
        tab_Xqj_cp.move(HFEDegI * NB_WORD_GFqn);
        prod.mul_gf2x(tab_Xqj_cp, acc);
        cur_acc.setXorRange(0, prod, 0, NB_WORD_MMUL);
        /* Final reduction of F(xv) */
        rem_gf2n(Fxv, 0, cur_acc);
        F.changeIndex(F_orig);
    }

    private void set0_high_product_gf2n(Pointer c)
    {
        if (NB_WORD_MMUL != NB_WORD_GFqn)
        {
            c.setRangeClear(NB_WORD_GFqn, NB_WORD_MMUL - NB_WORD_GFqn);
        }
    }
}


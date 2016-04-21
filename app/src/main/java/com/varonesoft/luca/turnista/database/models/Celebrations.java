package com.varonesoft.luca.turnista.database.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.varonesoft.luca.turnista.database.ContentProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by luca on 11/04/16.
 */
public class Celebrations extends DAO {

    // TAG
    private static String TAG = "DB Celebrations";

    // CONTENT TYPE
    public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.calendarioturni.celebrations";

    // BASE CODES
    public static final int BASEURICODE = 99001;
    public static final int BASEITEMCODE = 99011;

    // DATABASE TABLE
    public static final String TABLE_NAME = "tbl_celebrations";

    // PATH
    public static final String PATH= "celebrations";

    // Uri
    public static final Uri URI = Uri.withAppendedPath(
            Uri.parse(ContentProvider.SCHEME + ContentProvider.AUTHORITY), TABLE_NAME);

    // Add to the matcherUris thi codes
    public static void addMatcherUris(UriMatcher sURIMatcher) {
        sURIMatcher.addURI(ContentProvider.AUTHORITY, TABLE_NAME, BASEURICODE);
        sURIMatcher.addURI(ContentProvider.AUTHORITY, TABLE_NAME + "/#", BASEITEMCODE);
    }

    // Inner Class Columns
    public static class Columns implements BaseColumns {

        // This Class cannot be instantiated
        private Columns() {
        }

        // Table FIELDS
        public static final String NAME = "e_name";
        public static final String DESC = "e_desc";
        public static final String DATE = "e_date";
        public static final String ALLYEARS= "e_fixeddate";
        public static final String COLOR= "e_color";
        public static final String EXTRA = "e_extra";
        public static final String UPDATED = "e_updated";

        // Table Fields as Array
        public static final String[] FIELDS = { _ID, NAME, DESC, DATE, ALLYEARS,
                COLOR, EXTRA, UPDATED};
    }

    // Database creation SQL statement
    public static final String DATABASE_CREATE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(Columns._ID)             .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(Columns.NAME)            .append(" TEXT NOT NULL, ")
            .append(Columns.DESC)            .append(" TEXT DEFAULT NULL, ")
            .append(Columns.EXTRA)           .append(" TEXT DEFAULT NULL, ")
            .append(Columns.DATE)            .append(" INTEGER DEFAULT 0, ")
            .append(Columns.ALLYEARS)        .append(" INTEGER DEFAULT 0, ")
            .append(Columns.COLOR )          .append(" INTEGER DEFAULT NULL, ")
            .append(Columns.UPDATED)         .append(" INTEGER DEFAULT NULL);")
            .toString();

    // fields
    public String name = null;
    public String desc = null;
    public String extra = null;
    public Long date = null;
    public Long allyears = null;
    public Long color = null;
    public Long updated = null;

    // CONTRUCTOR
    public Celebrations (final Cursor c) {
        this.name = c.getString(1);
        this.desc = c.isNull(2) ? null : c.getString(2);
        this.extra = c.isNull(3) ? null : c.getString(3);
        this.date = c.isNull(4) ? null : c.getLong(4);
        this.allyears= c.isNull(5) ? null : c.getLong(5);
        this.color = c.isNull(6) ? null : c.getLong(6);
        this.updated = c.isNull(7) ? null : c.getLong(7);
    }

    public Celebrations(final Uri uri, final ContentValues values) {
        this(Long.parseLong(uri.getLastPathSegment()), values);
    }

    public Celebrations(final long id, final ContentValues values) {
        this(values);
        this._id = id;
    }

    public Celebrations(final ContentValues values) {
        name = values.getAsString(Columns.NAME);
        desc= values.getAsString(Columns.DESC);
        extra= values.getAsString(Columns.EXTRA);
        date= values.getAsLong(Columns.DATE);
        allyears= values.getAsLong(Columns.ALLYEARS);
        color = values.getAsLong(Columns.COLOR);
        updated = values.getAsLong(Columns.UPDATED);
    }

    @Override
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(Columns.NAME, name);
        values.put(Columns.DESC, desc);
        values.put(Columns.EXTRA, extra);
        values.put(Columns.DATE, date);
        values.put(Columns.ALLYEARS, allyears);
        values.put(Columns.COLOR, color);
        values.put(Columns.UPDATED, updated);
        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    /**
     * Convenience method for normal operations. Updates "updated" field.
     * Returns number of db-rows affected. Fail if < 1
     *
     * @param context
     */
    @Override
    public int save(Context context) {
        return save(context, Calendar.getInstance().getTimeInMillis());
    }

    public int save(Context context, final long update_time) {
        int result = 0;
        updated = update_time;
        if (_id < 1) {
            final Uri uri = context.getContentResolver().insert(getBaseUri(), getContent());
            if (uri != null) {
                _id = Long.parseLong(uri.getLastPathSegment());
                result++;
            }
        } else {
            result += context.getContentResolver().update(getUri(),
                    getContent(), null, null);
        }
        return result;
    }

    /* Init
     * put some default values into it
     */
    public static void init(SQLiteDatabase database) {
        final String[] names = new String[] {
                "Capodanno o Primo dell'Anno",
                "Epifania o Befana",
                "Pasqua",
                "Lunedì dell'Angelo o Pasquetta",
                "Anniversario della Liberazioneo o 25 Aprile",
                "Festa del Lavoro o Primo Maggio",
                "Festa della Repubblica",
                "Ferragosto o Assunzione",
                "Tutti i Santi (Ognissanti)",
                "Immacolata Concezione",
                "Natale",
                "Santo Stefano" };
        final String[] descs = new String[] {
                "Il Capodanno è la prima festività del nuovo anno. Nella stragrande maggioranza degli Stati moderni, è un giorno festivo e si festeggia il primo gennaio di ogni anno, secondo il Calendario Gregoriano.",
                "L'Epifania, detta anche festa della Befana è un giorno festivo in Italia e, più in generale, nei paesi cristiani, essendo una festività legata alla religione di Gesù Cristo, appunto. Si festeggia sempre il 6 gennaio, giorno in cui si ricorda la visita dei tre Re Magi all'appena nato Gesù bambino.",
                "La Pasqua è una delle festività più importanti del Cristianesimo. Essa ha lo scopo di celebrare la risurrezione di Gesù, così come narrata nella Bibbia e avvenuta il terzo giorno dopo la sua morte sulla collina chiamata Golgota-Calvario. In Italia, la Pasqua è un giorno festivo, ovvero un giorno in cui il lavoratore ha diritto a riposare, pur essendo retribuito. Va comunque fatto notare che, cadendo di domenica - che è sempre un giorno festivo - la Pasqua sarebbe un festivo a prescindere.",
                "Il lunedì dell'Angelo, in Italia conosciuto anche come \"Pasquetta\" oppure lunedì di Pasqua, è il primo lunedì successivo alla domenica di Pasqua. In Italia, è un giorno festivo, ovvero di riposo retribuito per i lavoratori. La sua introduzione avvenne nel dopoguerra, probabilmente per allungare le festività della Pasqua di un giorno",
                "L'anniversario della Liberazione, conosciuto anche come Festa della Liberazione, è un giorno festivo italiano nonché festa nazionale. Essa è conosciuta anche come anniversario della Resistenza, o semplicemente \"25 aprile\". In effetti, il 25 aprile è anche il giorno dell'anno in cui cade questa festività, proclamata ufficialmente tale a partire dall'anno 1946.\n" +
                        "\n" +
                        "La festa della Liberazione ha una connotazione politica molto importante e commemora un giorno fondamentale della storia della Repubblica Italiana, in quanto ricorda la resistenza dei partigiani che, durante la Seconda Guerra Mondiale, si opposero al governo fascista di Mussolini e all'occupazione tedesca da parte dei nazisti di Hitler.",
                "La festa del Lavoro, conosciuta anche come festa dei lavoratori, ma anche semplicemente come \"1° maggio\", è una festa celebrata in tutto il mondo Occidentale e in molti altri Paesi, per ricordare le battaglie degli operai per la conquista dell'orario di lavoro quotidiano, fissato in otto ore giornaliere.\n" +
                        "\n" +
                        "In Italia, così come anche nel resto dell'Europa, è un giorno festivo retribuito, ovvero un giorno in cui le attività lavorative sono sospese, ma i lavoratori percepiscono ugualmente la loro retribuzione o paga.",
                "La Festa della Repubblica Italiana è una festività italiana di natura civile, nata per festeggiare la nascita della Repubblica. In quanto festa nazionale, essa è un giorno di riposo retribuito, ovvero i lavoratori percepiscono regolarmente il loro salario o paga, pur non lavorando. Viene spesso confusa con la festa della Liberazione, che cade il 25 aprile, ma in realtà le due ricorrenze sono molto differenti.",
                "Il Ferragosto è una festività italiana che cade il 15 agosto, in concomitanza con la ricorrenza dell'Assunzione di Maria. Si tratta di un giorno festivo, ovvero indicato in rosso in calendario e pertanto giorno di riposo retribuito.",
                "Il 1° di novembre è un giorno festivo in Italia. Si celebra la festa cristiana di Ognissanti, anche conosciuta come \"Tutti i santi\", festività religiosa, ma anche civile. Infatti, il 1° di novembre è un giorno festivo, ovvero un giorno di lavoro retribuito, durante il quale i lavoratori hanno diritto a riposare, pur percependo la loro paga o retribuzione.",
                "L'Immacolata Concezione è una festività cattolica solenne, celebrata l'8 di dicembre. Tale festività celebra il dogma secondo il quale la Madonna è stata preservata immune dal peccato originale, in quanto solo una creatura priva di peccato sarebbe stata perfetta per portare in grembo il Figlio di Dio.\n" +
                        "\n" +
                        "In Italia, l'8 dicembre è un giorno festivo, ovvero un giorno di riposo retribuito durante il quale il lavoratore, seppur riposando, ha diritto a percepire la sua retribuzione.",
                "Il Santo Natale è una festa cristiana che celebra la nascità di Gesù Cristo, avvenuta il 25 dicembre (per le Chiese Occidentali e quelle Greco-Ortodosse), o il 6/7 gennaio per le Chiese Ortodosse che adottano il calendario giuliano, invece del calendario gregoriano.\n" +
                        "\n" +
                        "In Italia, il 25 dicembre è un giorno festivo, ovvero un giorno di riposo retribuito, e per questo segnato in rosso nel calendario. I lavoratori hanno diritto a riposare, pur percependo la loro retribuzione. La quale è maggiorata nei casi in cui il lavoratore non riposi, ma presti attività lavorativa.",
                "Santo Stefano è una festa liturgica che si celebra il 26 di dicembre, ovvero il giorno immediatamente successivo al Natale. La ragione di questa data sta nel fatto che Santo Stefano fu il primo martire della storia della cristianità e, per questo motivo, il suo nome è celebrato il 26 di dicembre, ovvero il primo giorno successivo alla nascita di Gesù.\n" +
                        "\n" +
                        "In Italia, il 26 di dicembre è un giorno festivo: i lavoratori salariati hanno diritto a riposare, pur percependo regolarmente la loro retribuzione. La quale risulta maggiorata se, durante tale giorno festivo, il lavoratore è tenuto a prestare attività lavorativa. Nel calendario, Santo Stefano è segnato in rosso."
        };
        final String[] extras = new String[] {
                "Origini della festa di Capodanno\n" +
                        "\n" +
                        "Si hanno traccia di festività legate all'inizio del nuovo anno sin dai tempi dell'antica Roma. Il culto del Capodanno era, allora, legato alla divinazione del dio pagano Giano, da cui deriva anche il nome del mese di gennaio. Giano era un dio con due facce, una delle quali guardava in avanti e l'altra indietro. Da ciò, si ritiene che Giano fosse il dio legato al Capodanno in quanto simboleggiava uno spartiacque tra il passato e il futuro.\n" +
                        "\n" +
                        "I festeggiamenti per celebrare l'inizio dell'anno erano molto diffusi anche durante il Medioevo. Essi affondavano le loro radici tanto nella cultura classica romana, quanto in quella druidica, soprattutto per quel che riguarda il nord Europa. Per questo motivo, essendo ritenuti di matrice pagana, vennero ben presto soppressi dalla Chiesa. Chiaramente, prima dell'introduzione del Calendario Gregoriano, il capodanno non aveva una data ben precisa e poteva variare da paese a paese. In ogni caso, le disparità locali fra paesi allora molto distanti - come i comuni e le repubbliche italiane, l'Inghilterra, la Francia e la Spagna - continuarono anche dopo l'adozione del Calendario Gregoriano e fino al 1691, quando Papa Innocenzo XII stabilì che l'inizio dell'anno doveva cadere il 1° gennaio, ovvero la data della circonsizione di Gesù Cristo.\n" +
                        "Il capodanno nel mondo moderno\n" +
                        "\n" +
                        "Oggigiorno, il capodanno viene festeggiato il 1° di gennaio nella stragrande maggioranza dei paesi occidentali. Nonostante cada una settimana dopo il Santo Natale, il capodanno è ancora oggi una festa più commerciale, che non di stampo religioso. Infatti, sebbene a livello clericale continui a corrispondere alla data della circoncisione di Gesù, la festa moderna ha poco di religioso e segna il passaggio dal vecchio al nuovo anno.\n" +
                        "Tradizioni di Capodanno\n" +
                        "\n" +
                        "In Italia, solitamente, il capodanno è preceduto dal Veglione di Capodanno, o vigilia di Capodanno. Le famiglie o gli amici si riuniscono insieme per una lauta cena che prepara i festeggiamenti veri e propri. Questi hanno solitamente inizio intorno alle dieci di sera del 31 dicembre, quando la gente inizia a riunirsi in piazza o per le strade di città per festeggiare. I festeggiamenti più comuni in queste ore sono i concerti nelle piazze o nelle discoteche, nonché i famigerati \"botti di capodanno\". Le principali emittenti televisive trasmettono programmi dedicati alla notte di Capodanno che culminano con il famoso \"countdown\" che separa l'anno vecchio da quello nuovo.\n" +
                        "\n" +
                        "Con il passaggio dal vecchio anno al nuovo i festeggiamenti non si concludono di certo, e anzi spesso vanno avanti per tutta la notte e fino all'alba del giorno dopo.\n" +
                        "\n" +
                        "Altre interessanti tradizioni sono:\n" +
                        "\n" +
                        "    mangiare lenticchie durante il veglione di Capodanno, in quanto si ritiene portino denaro per l'anno seguente;\n" +
                        "    mangiare chicchi d'uva. Questa tradizione ha origine spagnole;\n" +
                        "    indossare biancheria di colore rosso;\n" +
                        "\n" +
                        "Capodanno in Italia\n" +
                        "\n" +
                        "In Italia, come in molti altri paesi occidentali, il Capodanno è un giorno festivo, ovvero un giorno in cui le attività lavorative sono sospese, ma ugualmente retribuite ai lavoratori. Ciò non vale per il 31 di dicembre che, invece, è un giorno feriale ovvero lavorativo.",
                "Le origini dell'Epifania\n" +
                        "\n" +
                        "Il termine Epifania deriva dal greco antico e significa \"mi rendo manifesto\". In questo caso, è la venuta di Gesù Cristo ad essere resa manifesta e quindi omaggiata dai tre Re Magi. A partire dal III secolo d.C., l'Epifania di Gesù fu associata ai tre segni rivelatori della sua venuta: l'adorazione da parte dei tre Re Magi al Cristo appena nato a Betlemme, il battesimo di Gesù ormai adulto nel fiume Giordano e il suo primo miracolo a Cana.\n" +
                        "\n" +
                        "In seguito, la Chiesa di rito romano da cui ebbe origine l'attuale Cattolicesimo affermò che l'Epifania intesa come rivelazione di Gesù al mondo, grazie all'adorazione dei Magi, e il battesimo di Gesù erano due cose distinte e separate, e dovevano cadere in date separate.\n" +
                        "L'Epifania nel mondo moderno\n" +
                        "\n" +
                        "Oggigiorno, l'Epifania è ritenuta la festa che chiude il periodo natalizio ed ha assunto una connotazione maggiormente commerciale. Ci sono alcune simbologie e tradizioni che si ripetono. In particolare:\n" +
                        "\n" +
                        "    il simbolo della stella cometa, che si pensa guidò i tre Re Magi alla stalla di Betlemme dove si trovava Gesù;\n" +
                        "    lo scambio di doni legati, soprattutto in Italia, alla festa della Befana;\n" +
                        "    la tradizione dei regali fatti ai bambini, per lo più dolci da far trovare dentro una calza, ma solo nel caso in cui i bimbi siano buoni. Viceversa, viene regalato loro del carbone. In Italia, i doni sono portati dalla Befana, mentre in Spagna i doni sono portati dai Re Magi.\n" +
                        "    l'usanza di preparare dolci al forno dalle figurine più svariate, ma sempre legate all'Epifania, come il cammello, le palme, le pecorelle e ovviamente uno dei Re Magi. Secondo la tradizione francese delle \"galette des rois\", chi pescherà, fra i biscotti, la figura del Re avrà i privilegi di un re per tutto il giorno.\n" +
                        "\n" +
                        "La festività della Befana in Italia\n" +
                        "\n" +
                        "La Befana è un personaggio immaginario legato alle festività natalizie. Secondo la tradizione, si tratta di una vecchina che vola sulla scopa nella notte fra il 5 di gennaio e il 6; essa porta dolci e caramelle ai bambini che si sono comportati bene durante l'anno, o carbone e cenere a coloro che si sono comportati male.\n" +
                        "\n" +
                        "Le origini del culto della Befana sono probabilmente di natura pagana e l'accostamento fra la figura della Befana e quella di una strega buona è piuttosto immediato (cappello a punta, possibilità di volare su una scopa, aspetto esteriore brutto o spaventoso, abiti spesso neri). Ne troviamo traccia sin dal Medioevo, sebbene la Chiesa avesse tentato più volte di reprimerne il culto, considerato sacrilego.\n" +
                        "\n" +
                        "Nel XII secolo si cercò di cristianizzare il culto della Befana, introducendone la figura nel racconto dei Re Magi. Secondo questa versione, i tre re si erano persi lungo la strada che portava a Betlemme e chiesero aiuto ad una vecchina. La donna tuttavia si rifiutò di aiutarli. Nonostante tutto, i tre re Magi riuscirono ad arrivare alla grotta dove era nato Gesù, mentre la vecchina fu colta dal rimorso per non averli aiutati. Così preparò un cesto di dolci e si mise in viaggio nella speranza di ritrovare i Magi e la stalla dove era nato Gesù. Non sapendo cosa cercare, si fermava a regalare dolciumi a tutti i bambini che incontrava, perché non sapeva riconoscere chi fra essi fosse Gesù Cristo. Da allora, la Befana gira il mondo regalando dolci ai bambini, nella speranza di trovare il suo Gesù.",
                "Le origini della Pasqua Cristiana\n" +
                        "\n" +
                        "A differenza di altre festività, la Pasqua ha data variabile e si basa sul calendario lunare, così come la Pesach, la Pasqua ebraica, a cui è strettamente legata. Infatti, sempre secondo le Sacre Scritture, la passione e crocifissione di Gesù sarebbero avvenute proprio in coincidenza della Pasqua ebraica, che veniva celebrata in corrispondenza della luna piena di marzo/aprile. Durante il Concilio di Nicea, da una interpretazione delle sacre scritture di S. Paolo, si stabilì che la data della Pasqua cristiana doveva cadere la domenica successiva al plenilunio di marzo/aprile, ovvero in un arco di tempo che può andare dal 22 marzo al 25 aprile.\n" +
                        "\n" +
                        "Per questo motivo, la Pasqua non ha una data fissa. E lo stesso vale per altre festività legate alla Pasqua, quali la Pentecoste e la Quaresima.\n" +
                        "Preparazione alla Pasqua\n" +
                        "\n" +
                        "Secondo la liturgia, la Pasqua deve essere preceduta da un periodo di astinenza e digiuno che dura 40 giorni. Esso è chiamato Quaresima ed inizia con il mercoledì delle Ceneri. L'ultima settimana di Quaresima si chiama Settimana Santa. Essa è un periodo pieno di importanti ricorrenze liturgiche che iniziano con la Domenica delle Palme, per terminare con la domenica successiva, ovvero la domenica di Pasqua.\n" +
                        "Alcune tradizioni pasquali in Italia\n" +
                        "\n" +
                        "Fra le tradizioni pasquali del nostro Paese, ricordiamo soprattutto la benedizione delle palme e dell'ulivo che avviene durante la domenica delle Palme. Questa tradizione ricorda il giorno dell'ingresso di Gesù a Gerusalemme, dove fu accolto dai fedeli che omaggiavano il suo ingresso sventolando al cielo delle palme. I rametti d'ulivo benedetti durante la messa della Domenica delle Palme sono regalati ai fedeli per ricordare la passione di Cristo e vengono conservati tutto l'anno, in attesa di essere sostituiti dai rametti nuovi, l'anno successivo.\n" +
                        "\n" +
                        "Un'altra tradizione che ha assunto connotati sempre più commerciali è lo scambio delle uova di Pasqua. In principio si trattava di vere e proprie uova colorate che stavano ad indicare la rinascita, ovvero la Risurrezione di Cristo. Oggi, le uova sono state sostituite da uova di cioccolato spesso contenenti delle sorprese, regalate ai bambini italiani durante la Domenica di Pasqua.\n" +
                        "\n" +
                        "Un altro dolce legato a questa festività è la colomba pasquale. Essa ha origine in Lombardia, durante gli anni '30 del 1900. Si tratta di un dolce il cui impasto originale ricorda molto quello del panettone, infatti le prime colombe pasquali furono create dalla ditta milanese Motta che si occupava già nella produzione dei panettoni natalizi. La ditta milanese decise di creare un dolce per celebrare la Santa Pasqua, così all'impasto del panettone furono apportate delle modifiche, come l'aggiunta dell'aranciata candita che sostituì l'uva passa, e la glassatura di mandorle a ricoprire il dolce. La forma ricorda la colomba, simbolo di pace.",
                "Le origini della festa del lunedì dell'Angelo\n" +
                        "\n" +
                        "Il lunedì dell'Angelo deve il suo nome al fatto che, in base ai racconti del Nuovo Testamento, il giorno successivo alla crocifissione di Gesù Cristo vi fu l'incontro fra l'Angelo e le donne giunte al sepolcro.\n" +
                        "\n" +
                        "Narra il Vangelo che Maria di Magdala, Maria madre di Giacomo e Giuseppe, e Salomè si recarono al sepolcro di Gesù per imbalsamarne il corpo, ma quando giunsero al sepolcro, videro che il masso che ne bloccava l'ingresso era stato spostato. Allora apparve un angelo che disse loro che Cristo era risorto, esattamente come aveva annunciato. E che adesso toccava a loro dare la lieta notizia agli Apostoli.\n" +
                        "\n" +
                        "Probabilmente, vi è stato un errore di interpretazione dei fatti narrati nel Vangelo e l'incontro con l'angelo è stato spostato dalla mattina della domenica di Pasqua al giorno successivo, il lunedì, in quanto nel Vangelo si legge \"il giorno dopo la Pasqua\", sebbene nelle Sacre Scritture ci si riferisca alla Pasqua Ebraica che effettivamente cade di sabato. E non a quella cristiana che cade di domenica.\n" +
                        "Com'è festeggiato il lunedì dell'Angelo in Italia?\n" +
                        "\n" +
                        "In Italia, il giorno di Pasquetta è molto sentito, soprattutto dai lavoratori che possono concedersi un ulteriore giorno di riposo, dopo la Santa Pasqua. Esso viene trascorso per lo più in famiglia, o con amici. Se il bel tempo lo permette, soprattutto quando la Pasqua cade nel tardo mese di aprile, il lunedì di Pasquetta viene trascorso all'aperto, per delle scampagnate o gite \"fuori porta\". Solitamente, esso viene preparato con ampio anticipo dalle famiglie, che possono godersi i primi giorni di sole all'aperto. La tradizione vuole che si passi il lunedì di Pasqua con gli affetti più cari, in pic-nic e scampagnate che possono durare anche tutto il giorno.\n" +
                        "\n" +
                        "Probabilmente, anche questa usanza ha origini che affondano nella lettura del Vangelo. In accordo con la tradizione, essa rievoca l'incontro che avvenne fra Cristo risorto e due suoi discepoli, nei pressi del villaggio di Emmaus (Vangelo di Luca). Sempre secondo il Vangelo, i due discepoli vennero avvicinati da uno straniero (Gesù in realtà), che li accompagnò per tutto il tragitto che da Gerusalemme porta ad Emmaus. Tuttavia, i due non riconobbero chi era con loro finché, giunta la sera, non si fermarono in una locanda. Quando Gesù spezzò il pane, i due discepoli compresero chi li aveva accompagnati durante tutto il giorno, e solo allora la figura di Cristo sparì lasciandoli soli. I due tornarono a Gerusalemme per dare la lieta notizia agli altri discepoli e quando tutti furono riuniti, Gesù apparve nuovamente e li benedisse, prima di ascendere in cielo.\n" +
                        "\n" +
                        "Il lunedì dell'Angelo nel Mondo\n" +
                        "\n" +
                        "Questa festività non è solo conosciuta ed omaggiata in Italia, ma anche in numerosi paesi occidentali, soprattutto cristiani. Va tuttavia notato che il lunedì di Pasqua è festeggiato anche in altri paesi, non necessariamente cristiani, o non necessariamente di stampo cattolico. Ad esempio, la Chiesa Ortodossa e più in generale il Rito Bizantino riconoscono l'importanza del lunedì di Pasqua, chiamato anche Lunedì del Rinnovamento. I festeggiamenti legati a questo giorno includono, fra l'altro, processioni all'aperto in seguito alla liturgia.\n" +
                        "\n" +
                        "In Egitto, il giorno successivo alla Pasqua Cristiana è riconosciuto quale inizio della primavera e, per questo motivo, è considerato una festa nazionale. Prende il nome di Sham el-Nessim ed è festeggiato da tutti gli egiziani, qualsiasi sia il loro credo religioso (e quindi egiziani cristiani, o mussulmani).\n" +
                        "\n" +
                        "Negli Stati Uniti d'America, il lunedì dell'Angelo non è una festa nazionale. Tuttavia, va fatto notare che esso è particolarmente sentito dalle minoranze etniche di alcune città, come ad esempio dai cittadini di origini polacche che vivono a Chicago o Cleveland. Questo è dovuto al fatto che in Polonia il lunedì dopo Pasqua è particolarmente sentito e prende il nome di Śmigus dyngus, una ricorrenza durante la quale i cittadini polacchi aspergono i loro capi con l'acqua, simbolo di purezza e rinascita.",
                "Origini dell'anniversario della Liberazione d'Italia dalle forze naziste tedesche\n" +
                        "\n" +
                        "La festa della Liberazione ricorda il 25 aprile del 1945. Quel giorno, il Comitato di Liberazione Nazionale - che si era formato nel 1943 a Roma con lo scopo di contrastare il nazismo tedesco e la sua occupazione in Italia - proclamò via radio l'insurrezione partigiana contro i nazifasciti e i loro presidi. La rivolta fu tale che i soldati tedeschi e quelli della repubblica di Salò iniziarono a ritirarsi, soprattutto dalle grandi città del nord Italia, quali Milano e Torino, in cui la popolazione si era unita alla rivolta dei partigiani.\n" +
                        "\n" +
                        "La sera del 25 aprile, i partigiani occuparono la sede del giornale \"Il Corriere della Sera\" e usarono la sua tipografia per stampare i primi comunicati in cui si festeggiava un'Italia libera dalla dominazione straniera. Quella stessa sera, Mussolini abbandonò Milano per fuggire a Como, dove venne ucciso tre giorni dopo.\n" +
                        "\n" +
                        "Ovviamente, la liberazione del Paese non fu un fatto immediato e l'aiuto degli Alleati fu fondamentale per liberare l'intero Paese dall'oppressione stranisera. In effetti, le forze nazifasciste si arresero definitivamente solo circa dieci giorni dopo il 25 aprile, e più precisamente il 3 di maggio, come sancito dalla Resa di Caserta che, sebbene fu siglata il 29 aprile, divenne operativa solo a partire dai primi giorni del mese successivo.\n" +
                        "La festa della Liberazione oggi\n" +
                        "\n" +
                        "La festa della Liberazione d'Italia non è la festa della Repubblica, che invece cade il 2 di giugno. La sua importanza, tuttavia, non è da meno. Ogni anno, il Presidente della Repubblica fa omaggio alle tappe principali che ricordano la liberazione del nostro Paese dalle forze fasciste. Queste prevedono, nella città di Roma, la visita all'Altare della Patria in Piazza Venezia e l'omaggoi al monumento del Milite Ignoto. Poi ancora manifestazioni e sfilate in tutta la Capitale.\n" +
                        "\n" +
                        "Grandi festeggiamenti si tengono anche a Torino e a Milano, città che furono protagoniste proprio in quel lontano 25 aprile del 1945. Altri centri dove questa festività è particolarmente sentita sono le città decorate al valor militare, fra cui Bologna, Domodossola, Firenze, Gorizia e Imola, Piacenza, Parma ma anche Zara, oggi facente parte della Dalmazia Croata.\n" +
                        "Come si festeggia?\n" +
                        "\n" +
                        "In tutto il territorio italiano, molte sono le iniziative per festeggiare il 25 aprile, siano esse civili o religiose. Durante questo giorno, si ricordano i partigiani e più in generale tutte le persone che persero la loro vita per liberare l'Italia dalle forze nazifasciste di Hitler e Mussolini.\n" +
                        "\n" +
                        "I principali festeggiamenti includono concerti e musica, come ad esempio a Torino in piazza Castello, o cortei e manifestazioni come a Roma e Milano. Mentre a Roma è il Presidente della Repubblica a rendere omaggio al monumento del Milite Ignoto, a Milano è il sindaco a deporre una corona di fiori per ricordare i caduti per la libertà. Questa prassi è comune a tutte le città e i comuni d'Italia.\n" +
                        "\n" +
                        "Durante le manifestazioni, non è raro che prendano parte ai festeggiamenti gli ultimi partigiani del nostro Paese. Si tratta di persone ormai molto in là con gli anni che hanno vissuto in prima persona i drammi della Seconda Guerra Mondiale. Anche il corpo degli Alpini commemora questa festività, in ricordo dei giorni della Resistenza.",
                "Le origini americane della Festa del Lavoro\n" +
                        "\n" +
                        "Il 1° maggio ha origini americane e, più precisamente, questa festività nasce a Chicago nello stato dell'Illinois nel 1887. Le sue radici risalgono a una serie di manifestazioni, organizzate con cadenza annuale da un'associazione chiamata \"Knights of Labor\", ovvero i \"Cavalieri del lavoro\", a partire dal 1884. Tali lavoratori manifestavano per il riconoscimento di un tetto massimo di ore lavorative, fissato ad 8 ore giornaliere, e per il diritto al riposo fra un ciclo di lavoro e l'altro. Le loro richieste sfociarono nei sanguinosi incidenti del maggio 1886, durante la manifestazione passata alla storia con il nome di rivolta di Haymarket.\n" +
                        "\n" +
                        "I lavoratori riuniti in piazza Haymarket il 4 maggio 1886 protestavano per i diritti sindacali, e in particolare per combattere le condizioni miserabili del lavoro in fabbrica, cui spesso erano costretti per 10 o anche 12 ore al giorno, in condizioni precarie e con paghe ridotte al minimo. Ad essi si unì un gruppo di anarchici che trasformò la manifestazione in tragedia quando, al lancio di una bomba sulla polizia, le forze dell'ordine aprirono il fuoco uccidendo diversi manifestanti.\n" +
                        "\n" +
                        "In seguito a questi scontri, otto uomini vennero arrestati e condannati a morte per impiccagione, in quanto ritenuti anarchici e fomentatori. Solo in seguito, si scoprì che essi erano innocenti e le vere cause dietro gli incidenti di quel fatidico 1° di maggio rimangono tuttora avvolte nel mistero.\n" +
                        "\n" +
                        "Quella data, tuttavia, rimase nell'immaginario collettivo come la più rappresentativa per ricordare l'impegno dei movimenti sindacali e dei lavoratori nel far riconoscere quei diritti lavorativi che ancora oggi sono ritenuti fondamentali in tutto il mondo civilizzato.\n" +
                        "La festa del lavoro in Italia\n" +
                        "\n" +
                        "In Italia, quando nel 1888 i lavoratori livornesi appreso degli incidenti di Chicago, insorsero contro le navi statunitensi e contro la Questura dove si era nascosto il console americano. La reazione del governo Crispi fu aspra e furono adottate delle misure molto drastiche per impedire manifestazioin e cortei. Nonostante ciò, il 1° maggio del 1890 è una data storica, in Italia, in quanto i lavoratori riuscirono a coordinarsi e a dar vita ad una mobilitazione nazionale, con cortei e manifestazioni sparpagliati in tutto il territorio.\n" +
                        "\n" +
                        "Durante i primi decenni del 1900, le manifestazioni del 1° maggio si susseguirono in quasi tutta Europa e la rivendicazione originale del tetto massimo di 8 ore lavorative giornaliere, traguardo ormai raggiunto, venne messa da parte a favore di altre richieste di natura politica e sociale.\n" +
                        "\n" +
                        "Ma con l'avvento al potere di Benito Mussolini le cose cambiano e la festa dei lavoratori viene soppressa. O per meglio dire, essa venne spostata al 21 aprile e prese il nome di \"Natale di Roma\": una festività fascista, imbrigliata dal regime, che non aveva più alcun significato per i lavoratori.\n" +
                        "\n" +
                        "La situazione cambiò nuovamente con la liberazione del Paese dalla dominazione nazifascista, avvenuta il 25 aprile del 1945. Qualche giorno dopo, il 1° maggio del '45, i lavoratori e i partigiani scesero in piazza a festeggiare non solo la ricorrenza del 1° maggio ma anche la libertà italiana dal regime tedesco.\n" +
                        "\n" +
                        "Purtroppo, appena due anni dopo, un altro 1° maggio passò alla storia ed è tuttora tristemente ricordato in Italia. Si tratta del 1° maggio del 1947, quando gli uomini del bandito Giuliano aprirono il fuoco sulla folla di dimostranti.\n" +
                        "\n" +
                        "Durante gli anni '70, le pressioni sociali daranno nuova linfa ai movimenti dei lavoratori fino alle manifestazioni odierne così come le conosciamo.\n" +
                        "Il 1° maggio oggi\n" +
                        "\n" +
                        "In tempi recenti, il Primo Maggio continua ad essere una festa per commemorare i lavoratori e il diritto di ognuno al lavoro. Si festeggia con cortei e manifestazioni in tutto il Paese e con un grande concerto in Piazza San Giovanni a Roma, conosciuto anche come Concerto del Primo Maggio ed organizzato dalla CGIL, dalla CISL e dalla UIL.",
                "Le origini della Festa della Repubblica\n" +
                        "\n" +
                        "In seguito alla caduta di Mussolini e del fascismo, conseguenti alla liberazione del nostro paese da parte delle forze partigiane e alleate il 25 aprile 1945, i cittadini italiani furono chiamati al voto, per esprimere la loro preferenza su quale forma di governo avrebbe avuto il Paese. E' bene ricordare che, nonostante la dittatura Mussoliniana, l'Italia era ancora un regno, e più precisamente il Regno d'Italia. Di esso, erano sovrani i Savoia, proclamati famiglia reggente nel 1861 con Vittorio Emanuele II, primo Re d'Italia. Per 85 anni (di cui 20 di dittatura fascista), l'Italia era stata dunque una monarchia, ma gli eventi sanguinosi della Seconda Guerra Mondiale avevano cambiato gli scenari geopolitici del Mondo intero, e quindi era necessario donare un nuovo ordinamento istituzionale al paese.\n" +
                        "\n" +
                        "Per questi motivi, il 2 e il 3 di giugno dell'anno 1946, fu indetto un referendum a suffragio universale tramite cui tutti i cittadini con diritto di voto si espressero a favore della Repubblica. Nasceva così la Repubblica italiana, mentre la famiglia Savoia veniva mandata in esilio.\n" +
                        "La Festa della Repubblica in tempi più recenti\n" +
                        "\n" +
                        "Da allora, molte cose sono cambiate e anche se la festa della Repubblica riveste un ruolo molto importante da un punto di vista civile, in quanto commemora una svolta epocale per il nostro Paese, essa non è stata sempre festeggiata con gli onori che le sono dovuti.\n" +
                        "\n" +
                        "Ad esempio, durante gli anni '70, la festa della Repubblica venne soppressa a causa delle ristrettezze economiche in cui versava il nostro paese. O per meglio dire, venne spostata alla prima domenica di giugno, e quindi assorbita in un giorno di per sé sempre festivo (la domenica).\n" +
                        "\n" +
                        "Solo nel 2001, durante il secondo governo Amato, la Festa della Repubblica venne riportata alla sua data originaria.\n" +
                        "\n" +
                        "Oggi, la Festa della Repubblica continua ad essere un'importante ricorrenza civile e militare e ogni anno viene celebrata una storica parata a Roma che coinvolge tutte le più alte sfere politiche del nostro paese. Il tema della parata militare cambia ogni anno. Fra le più recenti, ricordiamo le parate del 2011, dedicata al \"150º anniversario dell’Unità d’Italia\", e quella del 2012, dedicata alle famiglie rimaste senza casa a seguito del terremoto che ha colpito l'Emilia nel maggio 2012.\n" +
                        "\n" +
                        "Come si festeggia la Festa della Repubblica\n" +
                        "\n" +
                        "Come già detto, la festa della Repubblica Italiana è una ricorrenza civile, quindi si tratta di una giornata dedicata a cerimonie ufficiali più o meno importanti. La parata militare storica avviene in via dei Fori Imperiali, a Roma, sin dal 1948. Sebbene da qualche anno i mezzi corazzati e alcuni altri corpi militari non sfilino più per rendera la parata meno costosa, essa conserva ancora un grande fascino, grazie ad iniziative speciali come il cambio solenne della Guardia d'Onore, che coinvolge i carabinieri a cavallo.\n" +
                        "\n" +
                        "Ma il momento più spettacolare dell'intera manifestazione è senza dubbio l'esibizione acrobatica delle Frecce Tricolori, sui cieli della capitale. Le loro scie di colore rosso, bianco e verde sono un simbolo storico della festa della Repubblica e sebbene in alcune occasioni le Frecce Tricolori non abbiamo preso parte all'evento, è proprio l'esibizione della pattuglia acrobatica nazionale a richiamare turisti e spettatori da ogni dove.\n" +
                        "\n" +
                        "I festeggiamenti proseguono anche nel pomeriggio, con l'apertura al pubblico dei giardini del Quirinale, dove si tengono concerti indetti dalle bande dei corpi militari del nostro paese. Il protagonista indiscusso, in questo caso, è l'inno di Mameli, o inno italiano.",
                "Le origini della festa di Ferragosto\n" +
                        "\n" +
                        "Le origini di questa festività affondano in un passato lontanissimo, ovvero quello dell'Impero Romano. Il Ferragosto fu istituito dall'imperatore Augusto nel 18 a.C., per celebrare la fine dei lavori agricoli stagionali. In effetti, il suo nome ha proprio il significato di \"feriae Augusti\", ovvero il \"riposo di Augusto\", in lingua latina.\n" +
                        "\n" +
                        "A quell'epoca, le finalità del ferragosto erano di natura esclusivamente \"materiale\" e servivano a garantire un adeguato periodo di riposo dopo le fatiche stagionali dei lavori nei campi. Ovviamente, Augusto trasse un notevole beneficio politico dall'introduzione di questa festa, rivolta principalmente ai contadini e ai lavoratori. La stessa, collegava i Vinalia rustica e i Consualia, prolungando notevolmente il periodo di riposo del mese di agosto.\n" +
                        "\n" +
                        "Quest'antica tradizione venne anche preservata durante il Medioevo. Tuttavia, nell'ottica di rendere meno pagana la ricorrenza, essa venne affiancata ai festeggiamenti dell'Assunzione della Madonna in cielo.\n" +
                        "Il Ferragosto oggi\n" +
                        "\n" +
                        "Oggi, il Ferragosto continua ad essere una festività particolarmente amata dagli italiani, coincidendo con la stagione del bel tempo e soprattutto con la parte centrale dell'estate. I festeggiamenti più comuni iniziano già nella notte del 14 agosto, con falò e fuochi d'artificio in spiaggia. Solitamente, questi festeggiamenti possono durare anche fino all'alba del giorno dopo e, per i più giovani, coincidono con una notte di campeggio o da passare in discoteca.\n" +
                        "\n" +
                        "I festeggiamenti del 15 prevedono, per lo più, lauti pranzi con i parenti o gite fuori porta, pic-nic e alcune manifestazioni che affondano le radici nel Medioevo, come pittoreschi pali, cortei e giostre in costume medievale, con la presenza anche di sbandieratori. Il più celebre fra questi, sebbene si tenga il giorno successivo - ovvero il 16 agosto - è il suggestivo Palio di Siena.\n" +
                        "L'Assunzione di Maria\n" +
                        "\n" +
                        "Come anticipato in precedenza, durante il Medioevo si cercò di rendere il ferragosto meno pagano. Questa festività venne quindi associata all'assunzione in Cielo della beata Vergine Maria. Tuttora, in Italia, il giorno di ferragosto coincide con la festività dedicata alla Madonna. Tale ricorrenza, di natura esclusivamente cattolica, ricorda il giorno in cui la Madonna fu assunta ovvero accolta in cielo, al termine della sua vita terrena.\n" +
                        "\n" +
                        "L'Assunzione di Maria è una ricorrenza di natura esclusivamente cattolica e viene rigettata dalle Chiese Anglicane e Protestanti. La Chiesa Ortodossa e quella Armena festeggiano invece la \"Dormizione di Maria Vergine\", ovvero la credenza che al termine della sua vita terrena, la Madonna sia caduta in un sonno profondo e quindi accolta in Cielo.\n" +
                        "\n" +
                        "In Italia, come anticipato, si festeggia l'Assunzione di Maria Vergine. Questa ricorrenza cade fra il 14 ed il 15 agosto. Essa è particolarmente sentita nel nostro paese e, in alcune circostanze, le festività possono iniziare addirittura il 13. Queste ultime annoverano: processioni, omaggi ai simulacri e alle statue della Madonna, celebrazioni della Santa Messa, ove possibile anche all'aperto.",
                "Le origini di Ognissanti e cosa simboleggia questa festa per la Cristianità\n" +
                        "\n" +
                        "Troviamo le prime tracce di questa ricorrenza cristiana già durante il IV secolo d.C., sebbene all'epoca la ricorrenza cadesse in primavera e non nel tardo autunno come avviene oggi. La festa di Tutti i Santi, esattamente come il nome suggerisce, commemora tutti i santi della storia della cristianità. Infatti, nel calendario civile italiano, ogni giorno è dedicato ad un santo o a un martire, ma, in realtà, i santi della cristianità sono molti di più di 365 e per questo motivo, non tutti possono trovare spazio nel calendario.\n" +
                        "\n" +
                        "Il 1° di novembre di ogni anno, si commemora il ricordo delle loro vite e dei loro martirii, ovvero si ricorda quanto importanti siano stati i loro sacrifici e i loro gesti per la storia cristiana.\n" +
                        "\n" +
                        "Fu papa Gregorio IV, nell'835 d.C., a richiedere espressamente all'allora re franco Luigi il Pio di ufficializzare questa celebrazione come festa di precetto e fissare la sua data il 1° di novembre di ogni anno. Cosa che effettivamente accadde ed è rimasta tuttora invariata, nonostante siano trascorsi più di mille anni.\n" +
                        "\n" +
                        "Vi sono delle interpretazioni molto controverse sul perché si sia scelta proprio tale giorno per commemorare tutti i santi della cristianità. Alcuni studi antropologici recenti fanno notare una certa continuità temporale con la festa celtica di \"Samhain\", da cui ha avuto origine Halloween. Samhain è il capodanno celtico e secondo le credenze di questo popolo, durante la notte a cavallo fra il 31 ottobre e il 1° novembre, i defunti tornano dall'oltretomba e camminano sulla terra, fra i viventi.\n" +
                        "\n" +
                        "Molto probabilmente, la Chiesa cristiana aveva bisogno di rendere meno pagana l'usanza di festeggiare il ritorno dei defunti dall'oltretomba che vigeva soprattutto nelle terre del nord Europa. Per far ciò, si decise di celebrare Ognissanti proprio il primo giorno del mese di novembre, in modo da cristinizzare gli strascichi di una ricorrenza che non apparteneva alla religione cristiana.\n" +
                        "In effetti, è interessante notare come il giorno successivo al 1° di novembre, vale a dire il 2 novembre, in Italia si celebri la festa dei defunti, nota anche come Giorno dei Morti, in cui i fedeli cristiani commemorano i cari che non ci sono più.\n" +
                        "Come si festeggia \"Tutti i Santi\" in Italia?\n" +
                        "\n" +
                        "La festa di Tutti i Santi è particolarmente sentita nel nostro paese. In tutto il territorio sono presenti celebrazioni e tradizioni che risalgono anche al Medioevo. Molte di queste sono di natura espressamente culinaria, con mostre dei prodotti di stagione e soprattutto dei dolci tipici della festa di Ognissanti.\n" +
                        "\n" +
                        "I festeggiamenti possono cambiare da regione a ragione. Ricordiamone alcuni:\n" +
                        "\n" +
                        "    In Sicilia, è convinzione che durante la notte di Ognissanti i defunti portino dolciumi ai bambini che si sono comportati bene. Fra questi ultimi, ricordiamo soprattutto i dolci di Martorana.\n" +
                        "    A Massa Carrara si crede che durante questa ricorrenza i proprietari delle cantine debbano dare un boccale di vino ai meno fortunati. I bambini indossano collane di mele e di caldarroste.\n" +
                        "    In Friuli Venezia Giulia si tiene una candela accesa, una bacinella d'acqua e un pezzo di pane a disposizione dei morti, qualora tornino dall'aldilà.\n" +
                        "    In Trentino Alto Adige si suonano le campane delle chiese per richiamare le anime dei defunti. A loro disposizione viene anche lasciata una tavola imbandita di ogni genere di leccornia.\n" +
                        "    L'usanza di lasciare la tavola imbandita per le anime dei morti è anche tipica della Sardena.\n",
                "Le origini della festa dell'Immacolata Concezione\n" +
                        "\n" +
                        "C'è molta confusione a proposito dell'Immacolata Concezione. Nell'immaginario comune, la gente tende a pensare che la concezione immacolata sia quella di Gesù Cristo. In realtà, seppur sia vero che la concezione di Cristo è immune dal peccato per natura stessa del Figlio di Dio, l'Immacolata Concezione che viene celebrata l'8 di dicembre non si riferisce al concepimento di Gesù, bensì ai natali della Vergine Maria. Secondo la Chiesa, ed in particolare in accordo con la bolla \"Ineffabilis Deus\" (1854) di Papa Pio IX, la Beata Vergine Maria è nata immune dal peccato originale, cui tutti siamo soggetti nell'attimo stesso del nostro concepimento. Tale destino è stato risparmiato alla Vergine perché solo in questo modo ella avrebbe potuto far da madre al Figlio di Dio, in Terra.\n" +
                        "\n" +
                        "È giusto notare che nella Bibba non c'è mai cenno al fatto che Maria sia stata concepita senza peccato, anzi, ella viene definita come una donna comune, come tante altre, ma prescelta da Dio per la nascita di suo Figlio. In realtà, l'istituto dell'Immacolata Concezione affonda le sue radici in due apparizioni mariane, entrambe riconosciute dalla Chiesa Cattolica.\n" +
                        "\n" +
                        "La prima apparizione avvenne a Parigi, nel 1830, ad una novizia di nome Catherine Labouré che, dopo l'apparizione della Madonna, fece coniare su suo ordine esplicito una medaglietta in cui è scritto \"o Maria, concepita senza peccato, pregate per noi che ricorriamo a Voi\".\n" +
                        "\n" +
                        "La seconda apparizione, invece, è forse la più famosa, e si riferisce all'apparizione della Beata Vergine a Lourdes, nel 1858. Fu proprio la Madonna a presentarsi alla pastorella Bernadette come l'\"Immacolata Concezione\", termine che la piccola Bernadette non poteva conoscere e che pertanto fu usato più volte per testimoniare la veridicità dell'apparizione stessa. Da chi altri, infatti, poteva aver appreso quella terminologia una piccola pastorella analfabeta, se non dalla Madonna in persona?\n" +
                        "Come si festeggia l'Immacolata Concezione in Italia\n" +
                        "\n" +
                        "La festa dell'Immacolata Concezione è particolarmente sentita in Italia, ed in particolare nel Sud della Penisola, in quanto l'Immacolata era la santa protettrice del Regno delle Due Sicilie, il regno che vigeva nel sud del paese prima dell'Unità.\n" +
                        "\n" +
                        "Tuttora, l'Immacolata Concezione è particolarmente sentita e rispettata in Campania, Calabria e Sicilia. Si festeggia soprattutto con celebrazioni ecclesiastiche, spesso solenni, durante le quali i credenti pregano ed omaggiano i simulacri della Madonna.\n" +
                        "\n" +
                        "In molti comuni, la statua della Madonna viene posta su dei supporti semoventi che vengono portati in processione per le vie della città. Spesso, le processioni sono caratterizzate da momenti di preghiera, recitazione del rosario, e canti. Nonché precedute o seguite dalla celebrazione della Santa Messa.\n" +
                        "\n" +
                        "Il giorno precedente all'8 di dicembre è conosciuto come Vigilia dell'Immacolata Concezione. Tradizione vuole che si pratichi l'astinenza dalla carne, per cui il 7 di dicembre si consumano per lo più piatti a base di pesce, come pesce azzurro e baccalà.",
                "Le origini del Santo Natale\n" +
                        "\n" +
                        "Come la maggiorparte delle festività della Chiesa Cattolica, anche il Natale non aveva una data ben precisa. Infatti, in nessuno dei Vangeli è indicata la data esatta della nascita di Gesù Cristo. Quando, nel IV secolo d.C., il cristianesimo divenne la religione ufficiale dell'Impero Romano, l'allora Papa Giulio I decise di utilizzare un periodo dell'anno gremito di feste pagane (il solstizio d'inverno per i Celti, i Saturnali per i Romani, le varie festività per celebrare i raccolti, nel Nord Europa...) per festeggiare la più solenne fra le feste Cristiane: i Natali di Cristo.\n" +
                        "\n" +
                        "La scelta di questo periodo dell'anno fu, probabilmente, premeditata. Lo scopo era quello di riuscire a sradicare le festività pagane, grazie alla solennità della più importante festa Crisiana.\n" +
                        "\n" +
                        "Va fatto notare che, sebbene da allora la data non sia cambiata, strascichi di tradizioni pagane sono ancora presenti durante i festeggiamenti del Natale: lo scambio di doni e lo stesso albero di natale sono tutti elementi che affondano le loro radici in un'epoca antecedente alla nascita di Gesù e del cristianesimo.\n" +
                        "Il Natale oggi: come si festeggia in Italia...\n" +
                        "\n" +
                        "Sebbene il Natale rappresenti la festa più importante del cristianesimo, oggi questa festività ha acquisito degli elementi che la rendono più laica e commerciale di quanto non fosse in precedenza.\n" +
                        "\n" +
                        "Innanzitutto, va fatto notare che il Natale cade in un periodo dell'anno tuttora pieno di ricorrenze e festività. Periodo dell'anno che annovera, fra le altre cose, il Capodanno, ovvero il passaggio dall'anno vecchio all'anno nuovo. E che viene chiuso dalla festa dell'Epifania, anch'essa storicamente legata alla nascita di Cristo e ritenuta la festività che celebra il battesimo di Gesù e l'arrivo dei Re Magi da Oriente.\n" +
                        "\n" +
                        "Oggi, il Natale ha assunto aspetti sempre più commerciali. È l'occasione migliore per riunirsi con la famiglia, mangiare insieme e scambiarsi i regali, spesso portati in dono da Babbo Natale, figura legata alle tradizioni nordiche.\n" +
                        "\n" +
                        "Durante tutto il periodo natalizio, le case e i giardini vengono addobbati con luci colorate e festoni. Lo stesso vale per le piazze, dove vengono predisposti presepi delle dimensioni più svariate e alberi di natale appositamente addobbati. L'addobbo dell'albero di Natale è una prassi ormai abbondantemente diffusa nel nostro paese: gli abeti, veri o finti, sono predisposti all'interno delle case e decorati con luci, palline e festoni colorati.\n" +
                        "\n" +
                        "Una tradizione particolarmente sentita, e che affonda le sue radici nel Medioevo, è quella dei presepi viventi. Essi sono tipici di tutta la penisola, e soprattutto dei centri medievali, che ben si prestano alla predispozione di scene natalizie che rievocano la nascita di Gesù.\n" +
                        "...e nel resto del Mondo\n" +
                        "\n" +
                        "Nei paesi del Nord Europa (come la Danimarca, la Svezia, l'Olanda, la Finlandia etc.) la tradizione di Babbo Natale e dei suoi folletti è particolarmente sentita e fa da protagonista durante i festeggiamenti di Natale. I bambini scrivono delle letterine da inviare a Santa Claus il quale, poi, distribuisce i doni grazie all'aiuto delle sue renne, guidate dalla renna Rudolph.\n" +
                        "\n" +
                        "Questa tradizione tipica del Nord Europa si è diffusa in maniera capillare in tutto il mondo, megli ultimi 70 anni, grazie soprattutto al cinema e alla televisione. Essa è oggi una delle tradizioni più conosciute e sentite, diffusa anche in paesi non cristiani, quali la Cina e il Giappone.\n" +
                        "\n" +
                        "Tipico dei paesi del Nord, della Germania e della Gran Bretagna è anche il calendario dell'Avvento, che i bambini sfogliano giorno dopo giorno, man a mano che il 25 dicembre si avvicina. In Gran Bretagna, i bambini appendono delle calze vuote ai caminetti, che poi saranno riempite da dolci e regali.\n" +
                        "\n" +
                        "In Spagna i festeggiamenti sono abbastanza fedeli a quelli italiani. La maggiore differenza sta nel fatto che lo scambio dei doni avviene in 6 gennaio, con l'arrivo dei Re Magi. La loro tradizione è particolarmente sentita nella penisola iberica, con tanto di sfilate a cavallo e manifestazioni in costume per ricordare il loro arrivo alla grotta di Betlemme.\n" +
                        "\n" +
                        "Nei paesi dell'emisfero australe, l'estate va dal 21 dicembre al 21 di marzo. Per questo motivo, il Natale cade in piena estate in paesi come l'Argentina, il Brasile o l'Australia.",
                "Origini della festa di Santo Stefano\n" +
                        "\n" +
                        "La festa di Santo Stefano rende omaggio al primo dei diaconi di Gerusalemme. Gli \"Atti degli Apostoli\" raccontano che, dopo la morte di Cristo, i dodici Apostoli erano completamente assorbiti dalla necessità di predicare la parola di Dio e, per questo motivo, non potevano dedicare tempo prezioso al \"servizio delle mense\". Per ovviare al vuoto che si veniva a creare, i dodici apostoli riunirono i loro discepoli e, fra questi, ne scelsero sette affinché si dedicassero al lavoro amministrativo e al popolo. Il primo fra questi sette fu proprio Stefano, uno dei primi ebrei ad essersi convertito al cristianesimo.\n" +
                        "\n" +
                        "Gli Atti degli Apostoli ci raccontano ancora come Stefano fosse un uomo pio e ben voluto fra i cristiani, ma particolarmente temuto fra gli ebrei. Egli era, ai loro occhi, colpevole di numerose conversioni di fede che avvenivano soprattutto fra i giudei soggetti a diaspora.\n" +
                        "\n" +
                        "Nell'anno 36, alcuni ebrei accusarono Stefano di blasfemia e lo trascinarono davanti al Sinedrio di Gerusalemme, affinché ne giudicasse gli atti. Ma durante il giudizio, Stefano in mistica adorazione acclamò il nome del Figlio di Dio, seduto alla testa del Padre, infervorando ancora di più il popolo. Gli Atti riportano ancora che gli ebrei presenti lo trascinarono via dal Sinedrio e lo lapidarono a morte con le pietre. Questo ci permette di datare il martirio come sicuramente avvenuto dopo la deposizione di Ponzio Pilato (36 d.C.), ovvero in quel periodo di vuoto amministrativo durante il quale Gerusalemme fu governata dal Sinedrio ebraico. Viceversa, se Stefano fosse stato ucciso prima, probabilmente sarebbe stato crocifisso come Cristo. La lapidazione, invece, essendo tipica del popolo ebreo del tempo, ci fornisce un'idea ben precisa riguardo il periodo della morte di Santo Stefano.\n" +
                        "Le reliquie di Santo Stefano e il culto del Santo\n" +
                        "\n" +
                        "Secoli dopo la sua morte, e più precisamente nel 415 d.C., il dotto Gamaliele apparve in sogno ad un sacerdote di nome Luciano di Kefar-Gamba. Gamaliele non era solo, bensì accompagnato da suo figlio Abiba, dal martire Stefano e dal suo discepolo Nicodemo. Il saggio rivelò al prete Luciano che lui e i suoi fratelli stavano soffrendo molto perché sepolti senza onore, ed indicò a Luciano il punto esatto in cui trovare le loro reliquie, in quel di Gerusalemme.\n" +
                        "\n" +
                        "Con l'accordo del vescovo della città, gli scavi presero il via e rivelarono davvero le reliquie dei santi, esattamente dove Gamaliele aveva rivelato si trovassero. Da allora, le reliquie furono spostate per tutto il mondo cristiano, generando stupore e scalpore e, cosa ben più importante, una grande quantità di miracoli, rendendo Stefano uno dei martiri più amati dell'intera cristianità.\n" +
                        "\n" +
                        "Solo in Italia, ben 14 comuni portano il nome di Santo Stefano, protettore di tagliapietre e muratori, nonché uno dei santi più amati nel nostro paese.\n" +
                        "Quando e come si festeggia Santo Stefano\n" +
                        "\n" +
                        "In quanto primo martire della storia della cristianità, la festa di Santo Stefano viene celebrata il primo giorno dopo Natale, ovvero il 26 di dicembre.\n" +
                        "\n" +
                        "In Italia, la festa di Santo Stefano è stata resa un festivo nel 1947, con lo scopo di prolungare le festività natalizie e rendere ancora più solenne il Santo Natale. Soltanto un'altra festa gode delle stesse peculiarità, ovvero la Santa Pasqua, i cui festeggiamenti sono prolungati di un giorno, grazie al Lunedì dell'Angelo anche noto come Pasquetta.\n" +
                        "\n" +
                        "Per quel che riguarda i festeggiamenti, la festa di Santo Stefano viene trascorsa in famiglia e con gli affetti più cari, esattamente come il Natale. Le attività durante questo giorno di festa non sono di molto dissimili a quelle del giorno precedente, con l'immancabile tombolata e i dolci natalizi. In caso di bel tempo, molti italiani scelgono di trascorrere Santo Stefano visitando le città d'arte del nostro paese, come Firenze, Venezia e Roma. Altrimenti, un'altra attività tipica della festa di Santo Stefano è quella di passare il pomeriggio al cinema."
        };
        final int start_days[][] = {
                {1,1,1},
                {6,1,1},
                {27,3,0},
                {28,3,0},
                {25,4,1},
                {1,5,1},
                {2,6,1},
                {15,8,1},
                {1,11,1},
                {8,12,1},
                {25,12,1},
                {26,12,1}
        };
        final long color = 0x33445566;
        final ContentValues values=new ContentValues();
        final Calendar cal = GregorianCalendar.getInstance();
        for (int i=0; i< names.length; i++) {
            values.clear();
            values.put(Columns.NAME, names[i]);
            values.put(Columns.DESC, descs[i]);
            values.put(Columns.EXTRA, extras[i]);
            cal.set(2016, start_days[i][1]-1, start_days[i][0]);
            values.put(Columns.DATE, cal.getTimeInMillis());
            values.put(Columns.ALLYEARS, start_days[i][2]);
            values.put(Columns.COLOR, color);
            values.put(Columns.UPDATED, Calendar.getInstance().getTimeInMillis());
            database.insert(TABLE_NAME, null, values);
            Log.d(TAG, String.format("Value inserted: %s", names[i]));
        }
    }
}

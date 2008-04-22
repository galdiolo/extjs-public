/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client;

import java.util.ArrayList;
import java.util.List;

public class TestData {

  public static String DUMMY_TEXT_SHORT = "<div style='padding:4 8px'>Lorem Ipsum is simply dummy text of the "
      + "printing and typesetting industry. Lorem Ipsum has been the industry's standard "
      + "dummy text ever since the 1500s.</div>";

  public static String DUMMY_TEXT_LONG = "<div class=text style='padding:2 8px'><p>Lorem ipsum dolor sit amet, consectetuer "
      + "adipiscing elit. Suspendisse velit metus, ultricies nec, aliquam quis, porttitor "
      + "a, felis. Donec est. Pellentesque urna dolor, bibendum nec, commodo a, laoreet sed, "
      + "turpis. Morbi tristique orci ac felis. Suspendisse nec tellus. Donec vitae quam "
      + "sed nibh luctus auctor. Suspendisse rhoncus lacus non magna. Proin consectetuer "
      + "arcu ac metus. Integer tristique erat id leo. Sed interdum dictum quam. Integer "
      + "eros. Vivamus eget elit. Quisque eget urna. Mauris venenatis molestie enim. "
      + "Aenean justo nisi, sodales vitae, pellentesque scelerisque, gravida vitae, diam. "
      + "<p>Curabitur pellentesque nulla et tellus. Fusce suscipit. Phasellus diam dolor, "
      + "ullamcorper a, placerat ac, elementum sit amet, arcu. In commodo. Duis vitae "
      + "justo vel quam nonummy imperdiet. Nam hendrerit convallis nisl. Praesent eu arcu. "
      + "Morbi justo. Proin semper venenatis nulla. Pellentesque habitant morbi tristique "
      + "senectus et netus et malesuada fames ac turpis egestas. Vivamus feugiat odio vitae "
      + "tortor. Mauris augue enim, volutpat vitae, aliquet eu, feugiat congue, nisl. Maecenas "
      + "ac orci. Donec malesuada. Proin nunc. Sed vitae urna. Nam ut mi. Nullam tempus vulputate ipsum. "
      + "Integer lacinia nonummy mauris. Nullam rhoncus accumsan nibh.</p><p>Fusce mattis. Donec "
      + "feugiat, lectus sit amet aliquet feugiat, nisi ante aliquam lacus, id facilisis metus nisl et "
      + "eros. Vestibulum tempor. Proin augue dui, commodo ut, aliquet non, tristique a, nulla. "
      + "Fusce dolor enim, bibendum at, placerat vel, ultricies vitae, libero. Praesent eu sem suscipit "
      + "dolor cursus gravida. Quisque tortor mauris, aliquam at, placerat at, iaculis non, ante. In "
      + "hendrerit, enim sed facilisis blandit, turpis erat lobortis velit, quis dapibus mauris "
      + "sapien ut nisl. Aliquam non leo eget elit ultrices ullamcorper. Aliquam porta, purus in "
      + "euismod vulputate, tellus pede imperdiet elit, vulputate viverra ipsum purus ac dolor. "
      + "Vivamus tempor lorem quis lorem. Maecenas et felis. Integer accumsan convallis est. Etiam ut "
      + "augue quis augue congue hendrerit. Vestibulum ante ipsum primis in faucibus orci luctus et "
      + "ultrices posuere cubilia Curae; Cras sem.</p><p>In hac habitasse platea dictumst. Donec facilisis rhoncus purus. "
      + "Suspendisse vulputate, nunc et mattis scelerisque, enim nisi imperdiet lectus, sed aliquet sapien nisl "
      + "feugiat tortor. Cras sit amet nisi. Vivamus dignissim. Integer a ligula. Morbi euismod. Aenean malesuada. "
      + "Pellentesque ut nisi eu purus egestas aliquam. Phasellus dolor augue, tempor a, rhoncus ac, accumsan ut, urna. "
      + "Aenean aliquet semper elit. Sed porta eros ac orci. Proin mollis dui iaculis felis. Suspendisse tortor nisi, "
      + "scelerisque at, adipiscing sagittis, vehicula et, sem. Etiam vulputate. Nullam vestibulum eros sed sapien. "
      + "<p>Duis molestie tempor arcu. Nam eu nunc. Vivamus at neque eu mi lobortis euismod. Sed erat pede, luctus a, "
      + "gravida quis, varius quis, ipsum. Proin vel massa. Cras auctor risus non nunc semper semper. Lorem ipsum dolor "
      + "sit amet, consectetuer adipiscing elit. Sed vel arcu. Sed consectetuer. Duis libero eros, imperdiet sed, "
      + "condimentum a, pretium nec, diam. Cum sociis natoque penatibus et magnis dis parturient montes, "
      + "nascetur ridiculus mus. Nunc egestas, urna nec interdum interdum, risus justo malesuada quam, vitae "
      + "consequat urna turpis at metus. Sed id neque eget diam euismod aliquet. Nam sed tortor. Praesent hendrerit "
      + "scelerisque dolor. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Phasellus semper, odio id "
      + "rutrum volutpat, mauris ante tempus metus, bibendum porta dolor ligula pretium tortor.</div>";

  public static Folder getTreeModel() {
    Folder[] folders = new Folder[] {
        new Folder("Beethoven",
            new Folder[] {

                new Folder("Quartets",
                    new Music[] {
                        new Music("Six String Quartets", "Beethoven", "Quartets"),
                        new Music("Three String Quartets", "Beethoven", "Quartets"),
                        new Music("Grosse Fugue for String Quartets", "Beethoven",
                            "Quartets"),}),
                new Folder("Sonatas", new Music[] {
                    new Music("Sonata in A Minor", "Beethoven", "Sonatas"),
                    new Music("Sonata in F Major", "Beethoven", "Sonatas"),}),

                new Folder("Concertos", new Music[] {
                    new Music("No. 1 - C", "Beethoven", "Concertos"),
                    new Music("No. 2 - B-Flat Major", "Beethoven", "Concertos"),
                    new Music("No. 3 - C Minor", "Beethoven", "Concertos"),
                    new Music("No. 4 - G Major", "Beethoven", "Concertos"),
                    new Music("No. 5 - E-Flat Major", "Beethoven", "Concertos"),}),

                new Folder("Symphonies", new Music[] {
                    new Music("No. 1 - C Major", "Beethoven", "Symphonies"),
                    new Music("No. 2 - D Major", "Beethoven", "Symphonies"),
                    new Music("No. 3 - E-Flat Major", "Beethoven", "Symphonies"),
                    new Music("No. 4 - B-Flat Major", "Beethoven", "Symphonies"),
                    new Music("No. 5 - C Minor", "Beethoven", "Symphonies"),
                    new Music("No. 6 - F Major", "Beethoven", "Symphonies"),
                    new Music("No. 7 - A Major", "Beethoven", "Symphonies"),
                    new Music("No. 8 - F Major", "Beethoven", "Symphonies"),
                    new Music("No. 9 - D Minor", "Beethoven", "Symphonies"),}),}),
        new Folder("Brahms",
            new Folder[] {
                new Folder("Concertos", new Music[] {
                    new Music("Violin Concerto", "Brahms", "Concertos"),
                    new Music("Double Concerto - A Minor", "Brahms", "Concertos"),
                    new Music("Piano Concerto No. 1 - D Minor", "Brahms", "Concertos"),
                    new Music("Piano Concerto No. 2 - B-Flat Major", "Brahms",
                        "Concertos"),}),
                new Folder("Quartets",
                    new Music[] {
                        new Music("Piano Quartet No. 1 - G Minor", "Brahms", "Quartets"),
                        new Music("Piano Quartet No. 2 - A Major", "Brahms", "Quartets"),
                        new Music("Piano Quartet No. 3 - C Minor", "Brahms", "Quartets"),
                        new Music("String Quartet No. 3 - B-Flat Minor", "Brahms",
                            "Quartets"),}),
                new Folder("Sonatas", new Music[] {
                    new Music("Two Sonatas for Clarinet - F Minor", "Brahms", "Sonatas"),
                    new Music("Two Sonatas for Clarinet - E-Flat Major", "Brahms",
                        "Sonatas"),}),
                new Folder("Symphonies", new Music[] {
                    new Music("No. 1 - C Minor", "Brahms", "Symphonies"),
                    new Music("No. 2 - D Minor", "Brahms", "Symphonies"),
                    new Music("No. 3 - F Major", "Brahms", "Symphonies"),
                    new Music("No. 4 - E Minor", "Brahms", "Symphonies"),}),}),
        new Folder("Mozart", new Folder[] {new Folder("Concertos", new Music[] {
            new Music("Piano Concerto No. 12", "Mozart", "Concertos"),
            new Music("Piano Concerto No. 17", "Mozart", "Concertos"),
            new Music("Clarinet Concerto", "Mozart", "Concertos"),
            new Music("Violin Concerto No. 5", "Mozart", "Concertos"),
            new Music("Violin Concerto No. 4", "Mozart", "Concertos"),}),}),};

    Folder root = new Folder("root");
    for (int i = 0; i < folders.length; i++) {
      root.add((Folder) folders[i]);
    }

    return root;
  }

  public static List<Stock> getStocks() {
    List<Stock> stocks = new ArrayList<Stock>();
    
    stocks.add(new Stock("Apple Inc.", "AAPL", 125.64, 123.43));
    stocks.add(new Stock("Cisco Systems, Inc.", "CSCO", 25.84, 26.3));
    stocks.add(new Stock("Google Inc.", "GOOG", 516.2, 512.6));
    stocks.add(new Stock("Intel Corporation", "INTC", 21.36, 21.53));
    stocks.add(new Stock("Level 3 Communications, Inc.", "LVLT", 5.55, 5.54));
    stocks.add(new Stock("Microsoft Corporation", "MSFT", 29.56, 29.72));
    stocks.add(new Stock("Nokia Corporation (ADR)", "NOK", 27.83, 27.93));
    stocks.add(new Stock("Oracle Corporation", "ORCL", 18.73, 18.98));
    stocks.add(new Stock("Starbucks Corporation", "SBUX", 27.33, 27.36));
    stocks.add(new Stock("Yahoo! Inc.", "YHOO", 26.97, 27.29));
    stocks.add(new Stock("Applied Materials, Inc.", "AMAT", 18.4, 18.66));
    stocks.add(new Stock("Comcast Corporation", "CMCSA", 25.9, 26.4));
    stocks.add(new Stock("Sirius Satellite", "SIRI", 2.77, 2.74));
    
    stocks.add(new Stock("Tellabs, Inc.", "TLAB", 10.64, 10.75));
    stocks.add(new Stock("eBay Inc.", "EBAY", 30.43, 31.21));
    stocks.add(new Stock("Broadcom Corporation", "BRCM", 30.88, 30.48));
    stocks.add(new Stock("CMGI Inc.", "CMGI", 2.14, 2.13));
    stocks.add(new Stock("Amgen, Inc.", "AMGN", 56.22, 57.02));
    stocks.add(new Stock("Limelight Networks", "LLNW", 23, 22.11));
    stocks.add(new Stock("Amazon.com, Inc.", "AMZN", 72.47, 72.23));
    
    stocks.add(new Stock("E TRADE Financial Corporation", "ETFC", 24.32, 24.58));
    stocks.add(new Stock("AVANIR Pharmaceuticals", "AVNR", 3.7, 3.52));
    stocks.add(new Stock("Gemstar-TV Guide, Inc.", "GMST", 4.41, 4.55));
    stocks.add(new Stock("Akamai Technologies, Inc.", "AKAM", 43.08, 45.32));
    stocks.add(new Stock("Motorola, Inc.", "MOT", 17.74, 17.69));
    stocks.add(new Stock("Advanced Micro Devices, Inc.", "AMD", 13.77, 13.98));
    stocks.add(new Stock("General Electric Company", "GE", 36.8, 36.91));
    stocks.add(new Stock("Texas Instruments Incorporated", "TXN", 35.02, 35.7));
    stocks.add(new Stock("Qwest Communications", "Q", 9.9, 10.03));
    stocks.add(new Stock("Tyco International Ltd.", "TYC", 33.48, 33.26));
    stocks.add(new Stock("Pfizer Inc.", "PFE", 26.21, 26.19));
    stocks.add(new Stock("Time Warner Inc.", "TWX", 20.3, 20.45));
    stocks.add(new Stock("Sprint Nextel Corporation", "S", 21.85, 21.76));
    stocks.add(new Stock("Bank of America Corporation", "BAC", 49.92, 49.73));
    stocks.add(new Stock("Taiwan Semiconductor", "TSM", 10.4, 10.52));
    stocks.add(new Stock("AT&T Inc.", "T", 39.7, 39.66));
    stocks.add(new Stock("United States Steel Corporation", "X", 115.81, 114.62));
    stocks.add(new Stock("Exxon Mobil Corporation", "XOM", 81.77, 81.86));
    stocks.add(new Stock("Valero Energy Corporation", "VLO", 72.46, 72.6));
    stocks.add(new Stock("Micron Technology, Inc.", "MU", 12.02, 12.27));
    stocks.add(new Stock("Verizon Communications Inc.", "VZ", 42.5, 42.61));
    stocks.add(new Stock("Avaya Inc.", "AV", 16.96, 16.96));
    stocks.add(new Stock("The Home Depot, Inc.", "HD", 37.66, 37.79));
    
    stocks.add(new Stock("First Data Corporation", "FDC", 32.7, 32.65));
    return stocks;

  }

  private static final int NUM_ITEMS = 37;
  private static final int FRAGMENTS_PER_EMAIL = 10;

  private static final String[] senders = new String[] {
      "markboland05", "Hollie Voss", "boticario", "Emerson Milton", "Healy Colette",
      "Brigitte Cobb", "Elba Lockhart", "Claudio Engle", "Dena Pacheco", "Brasil s.p",
      "Parker", "derbvktqsr", "qetlyxxogg", "antenas.sul", "Christina Blake",
      "Gail Horton", "Orville Daniel", "PostMaster", "Rae Childers", "Buster misjenou",
      "user31065", "ftsgeolbx", "aqlovikigd", "user18411", "Mildred Starnes",
      "Candice Carson", "Louise Kelchner", "Emilio Hutchinson", "Geneva Underwood",
      "Residence Oper?", "fpnztbwag", "tiger", "Heriberto Rush", "bulrush Bouchard",
      "Abigail Louis", "Chad Andrews", "bjjycpaa", "Terry English", "Bell Snedden",
      "huang", "hhh", "(unknown sender)", "Kent", "Dirk Newman", "Equipe Virtual Cards",
      "wishesundmore", "Benito Meeks"};

  private static final String[] emails = new String[] {
      "mark@example.com", "hollie@example.com", "boticario@example.com",
      "emerson@example.com", "healy@example.com", "brigitte@example.com",
      "elba@example.com", "claudio@example.com", "dena@example.com",
      "brasilsp@example.com", "parker@example.com", "derbvktqsr@example.com",
      "qetlyxxogg@example.com", "antenas_sul@example.com", "cblake@example.com",
      "gailh@example.com", "orville@example.com", "post_master@example.com",
      "rchilders@example.com", "buster@example.com", "user31065@example.com",
      "ftsgeolbx@example.com", "aqlovikigd@example.com", "user18411@example.com",
      "mildred@example.com", "candice@example.com", "louise_kelchner@example.com",
      "emilio@example.com", "geneva@example.com", "residence_oper@example.com",
      "fpnztbwag@example.com", "tiger@example.com", "heriberto@example.com",
      "bulrush@example.com", "abigail_louis@example.com", "chada@example.com",
      "bjjycpaa@example.com", "terry@example.com", "bell@example.com",
      "huang@example.com", "hhh@example.com", "kent@example.com", "newman@example.com",
      "equipe_virtual@example.com", "wishesundmore@example.com", "benito@example.com"};

  private static final String[] subjects = new String[] {
      "URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]",
      "URGENT TRANSACTION -[Sun, 23 Apr 2006 13:10:03 +0000]", "fw: Here it comes",
      "voce ganho um vale presente Boticario", "Read this ASAP", "Hot Stock Talk",
      "New Breed of Equity Trader", "FWD: TopWeeks the wire special pr news release",
      "[fwd] Read this ASAP", "Renda Extra R$1.000,00-R$2.000,00/m?s",
      "re: Make sure your special pr news released", "Forbidden Knowledge Conference",
      "decodificadores os menores pre?os", "re: Our Pick",
      "RE: The hottest pick Watcher",
      "RE: St0kkMarrkett Picks Trade watch special pr news release",
      "St0kkMarrkett Picks Watch special pr news release news",
      "You are a Winner oskoxmshco", "Encrypted E-mail System (VIRUS REMOVED)",
      "Fw: Malcolm", "Secure Message System (VIRUS REMOVED)",
      "fwd: St0kkMarrkett Picks Watch special pr news releaser",
      "FWD: Financial Market Traderr special pr news release",
      "? s? uma dica r?pida !!!!! leia !!!", "re: You have to heard this",
      "fwd: Watcher TopNews", "VACANZE alle Mauritius", "funny",
      "re: You need to review this", "[re:] Our Pick",
      "RE: Before the be11 special pr news release",
      "[re:] Market TradePicks Trade watch news", "No prescription needed",
      "Seu novo site", "[fwd] Financial Market Trader Picker",
      "FWD: Top Financial Market Specialists Trader interest increases",
      "Os cart?es mais animados da web!!", "We will sale 4 you cebtdbwtcv",
      "RE: Best Top Financial Market Specialists Trader Picks"};

  private static final String[] fragments = new String[] {
      "Dear Friend,<br><br>I am Mr. Mark Boland the Bank Manager of ABN AMRO "
          + "BANK 101 Moorgate, London, EC2M 6SB.<br><br>",
      "I have an urgent and very confidential business proposition for you. On "
          + "July 20, 2001; Mr. Zemenu Gente, a National of France, who used to be a "
          + "private contractor with the Shell Petroleum Development Company in Saudi "
          + "Arabia. Mr. Zemenu Gente Made a Numbered time (Fixed deposit) for 36 "
          + "calendar months, valued at GBP?30, 000,000.00 (Thirty Million Pounds "
          + "only) in my Branch.",
      "I have all necessary legal documents that can be used to back up any "
          + "claim we may make. All I require is your honest Co-operation, "
          + "Confidentiality and A trust to enable us sees this transaction through. "
          + "I guarantee you that this will be executed under a legitimate "
          + "arrangement that will protect you from any breach of the law. Please "
          + "get in touch with me urgently by E-mail and "
          + "Provide me with the following;<br>",
      "The OIL sector is going crazy. This is our weekly gift to you!<br>" + "<br>"
          + "Get KKPT First Thing, This Is Going To Run!<br>" + "<br>"
          + "Check out Latest NEWS!<br>" + "<br>"
          + "KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>"
          + "Our last pick gained $2.16 in 4 days of trading.<br>",
      "LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. "
          + "(Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that "
          + "its operator for the Corsicana Field, JMT Resources, Ltd. (\"JMT\") "
          + "will commence a re-work program on its Pecan Gap wells in the next week. "
          + "The re-work program will consist of drilling six lateral bore production "
          + "strings from the existing well bore. This process, known as Radial Jet "
          + "Enhancement, will utilize high pressure fluids to drill the lateral well "
          + "bores, which will extend out approximately 350\' each.",
      "JMT has contracted with Well Enhancement Services, LLC (www."
          + "wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. "
          + "A small sand frac will follow the drilling of the lateral well bores in "
          + "order to enhance permeability and create larger access to the Pecan Gap "
          + "reservoir. Total cost of the re-work per well is estimated to be "
          + "approximately $50,000 USD.",
      "Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de "
          + "R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - "
          + "Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), "
          + "Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, "
          + "fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o "
          + "arquivo previamente salvo, imprima uma folha e salve a c?pia em seu "
          + "computador para evitar transtornos decorrentes da perda do mesmo. "
          + "Lembramos que o Vale-Presente ? ?nico e intransfer?vel.",
      "Large Marketing Campaign running this weekend!<br>" + "<br>"
          + "Should you get in today before it explodes?<br>" + "<br>"
          + "This Will Fly Starting Monday!",
      "PREMIER INFORMATION (PIFR)<br>"
          + "A U.S. based company offers specialized information management "
          + "serices to both the Insurance and Healthcare Industries. The services "
          + "we provide are specific to each industry and designed for quick "
          + "response and maximum security.<br>" + "<br>" + "STK- PIFR<br>"
          + "Current Price: .20<br>"
          + "This one went to $2.80 during the last marketing Campaign!",
      "These partnerships specifically allow Premier to obtain personal health "
          + "information, as governed by the Health In-surancee Portability and "
          + "Accountability Act of 1996 (HIPAA), and other applicable state laws and "
          + "regulations.<br><br>"
          + "Global HealthCare Market Undergoing Digital Conversion",
      ">>   Componentes e decodificadores; confira aqui;<br>"
          + " http://br.geocities.com/listajohn/index.htm<br>",
      "THE GOVERNING AWARD<br>" + "NETHERLANDS HEAD OFFICE<br>" + "AC 76892 HAUITSOP<br>"
          + "AMSTERDAM, THE NETHERLANDS.<br>"
          + "FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>"
          + "INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>"
          + "REF NUMBER: 14235/089.<br>" + "BATCH NUMBER: 304/64780/IFY.<br>"
          + "RE/AWARD NOTIFICATION<br>",
      "We are pleased to inform you of the announcement today 13th of April "
          + "2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on "
          + "the 28th of March 2006. The THREE Winning Addresses were randomly "
          + "selected from a batch of 10,000,000 international email addresses. "
          + "Your email address emerged alongside TWO others as a category B winner "
          + "in this year\'s Annual GOVERNING AWARD Draw.<br>",
      ">> obrigado por me dar esta pequena aten??o !!!<br>"
          + "CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS "
          + "LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES "
          + "DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , "
          + "TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E "
          + "ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , "
          + "DOCUMENT?RIOS ,SHOWS , ETC,<br><br>"
          + "CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>"
          + "DECODERS NET TVA DESBLOQUEADOS                       390,00<br>"
          + "KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>"
          + "TECSAT FREE   450,00<br>" + "TENHO TB ACESS?RIOS , CABOS, LNB .<br>",
      "********************************************************************<br>"
          + " Original filename: mail.zip<br>" + " Virus discovered: JS.Feebs.AC<br>"
          + "********************************************************************<br>"
          + " A file that was attached to this email contained a virus.<br>"
          + " It is very likely that the original message was generated<br>"
          + " by the virus and not a person - treat this message as you would<br>"
          + " any other junk mail (spam).<br>"
          + " For more information on why you received this message please visit:<br>",
      "Put a few letters after your name. Let us show you how you can do it in "
          + "just a few days.<br><br>" + "http://thewrongchoiceforyou.info<br><br>"
          + "kill future mailing by pressing this : see main website",
      "We possess scores of pharmaceutical products handy<br>"
          + "All med\'s are made in U.S. laboratories<br>"
          + "For your wellbeing! Very rapid, protected and secure<br>"
          + "Ordering, No script required. We have the pain aid you require<br>",
      "\"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, "
          + "but Austria never has wished, and does not wish, for war. She is "
          + "betraying us! Russia alone must save Europe. Our gracious sovereign "
          + "recognizes his high vocation and will be true to it. That is the one "
          + "thing I have faith in! Our good and wonderful sovereign has to perform "
          + "the noblest role on earth, and he is so virtuous and noble that God "
          + "will not forsake him. He will fulfill his vocation and crush the hydra "
          + "of revolution, which has become more terrible than ever in the person of "
          + "this murderer and villain! We alone must avenge the blood of the "
          + "just one.... Whom, I ask you, can we rely on?... England with "
          + "her commercial spirit will not and cannot understand the Emperor "
          + "Alexander\'s loftiness of soul. She has refused to evacuate Malta. "
          + "She wanted to find, and still seeks, some secret motive in our "
          + "actions. What answer did Novosiltsev get? None. The English have not "
          + "understood and cannot understand the self-ab!<br>negation of our "
          + "Emperor who wants nothing for himself, but only desires the good "
          + "of mankind. And what have they promised? Nothing! And what little "
          + "they have promised they will not perform! Prussia has always "
          + "declared that Buonaparte is invincible, and that all Europe is "
          + "powerless before him.... And I don\'t believe a word that Hardenburg "
          + "says, or Haugwitz either. This famous Prussian neutrality is just a "
          + "trap. I have faith only in God and the lofty destiny of our adored "
          + "monarch. He will save Europe!\"<br>\"Those were extremes, no doubt, "
          + "but they are not what is most important. What is important are the "
          + "rights of man, emancipation from prejudices, and equality of "
          + "citizenship, and all these ideas Napoleon has retained in full "
          + "force.\""};

  private static int senderIdx = 0, emailIdx = 0, subjectIdx = 0, fragmentIdx = 0;
  private static ArrayList<MailItem> items = new ArrayList<MailItem>();

  static {
    for (int i = 0; i < NUM_ITEMS; ++i)
      items.add(createFakeMail());
  }

  public static List<MailItem> getMailItems() {
    List<MailItem> list = new ArrayList<MailItem>();
    int size = items.size();
    for (int i = 0; i < size; i++) {
      list.add(items.get(i));
    }
    return list;
  }

  private static MailItem createFakeMail() {
    String sender = senders[senderIdx++];
    if (senderIdx == senders.length) senderIdx = 0;

    String email = emails[emailIdx++];
    if (emailIdx == emails.length) emailIdx = 0;

    String subject = subjects[subjectIdx++];
    if (subjectIdx == subjects.length) subjectIdx = 0;

    String body = "";
    for (int i = 0; i < FRAGMENTS_PER_EMAIL; ++i) {
      body += fragments[fragmentIdx++];
      if (fragmentIdx == fragments.length) fragmentIdx = 0;
    }

    return new MailItem(sender, email, subject, body);
  }
}
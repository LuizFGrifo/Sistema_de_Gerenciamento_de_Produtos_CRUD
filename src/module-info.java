module GUI {
	exports modelo.dao;
	exports modelo.entidades;
	requires java.desktop;
	requires transitive java.sql;
	requires java.sql.rowset;
}
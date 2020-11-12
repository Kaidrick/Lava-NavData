package moe.ofs.addon.navdata;

import moe.ofs.backend.Configurable;

import moe.ofs.backend.repositories.PlayerInfoRepository;

/**
 * Read use defined data from dcs write path
 * The format of the data should be either in json or in xml
 */
public class SupplementDataManager implements Configurable {
    private String dataFileName = "supplement_nav_data";

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    @Override
    public String getName() {
        return dataFileName;
    }
}

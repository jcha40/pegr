package pegr;

class RunStats {
    Float libraryVolume
    Float libraryStock
    Float libraryStdDev
    Float pctLibraryStdDev
    String cycles
    String srOrPe
    String seqCtrl
    Integer pcrCycles
    Float qubitConc
    Float qPcrConc
    Float libraryLoadedPm
    Float phiXLoaded
    Float libraryLoadedFmol
    Float clusterNum
    Float readPf
    Float pctPf
    Float pctQ30
    Float qidx
    Float totalReads
    Float unmatchedIndices
    Float pctUnmatchedIndices
    Float pctAlignedToPhiX    
    String libraryPoolArchiveId
    Date qPcrDate
    User technician
    String notes
    
    static constraints = {
        libraryVolume nullable: true
        libraryStock nullable: true
        libraryStdDev nullable: true
        pctLibraryStdDev nullable: true
        cycles nullable: true
        srOrPe nullable: true
        seqCtrl nullable: true, blank: true
        pcrCycles nullable: true
        qubitConc nullable: true
        qPcrConc nullable: true
        libraryLoadedPm nullable: true
        phiXLoaded nullable: true
        libraryLoadedFmol nullable: true
        clusterNum nullable: true
        readPf nullable: true
        pctPf nullable: true
        pctQ30 nullable: true
        qidx nullable: true
        totalReads nullable: true
        unmatchedIndices nullable: true
        pctUnmatchedIndices nullable: true
        pctAlignedToPhiX nullable: true
        libraryPoolArchiveId nullable: true
        qPcrDate nullable: true
        technician nullable: true
        notes nullable: true
    }
}
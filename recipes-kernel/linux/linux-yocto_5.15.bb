KBRANCH ?= "v5.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.15/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.15/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.15/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.15/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.15/standard/base"
KBRANCH_qemuriscv32  ?= "v5.15/standard/base"
KBRANCH_qemux86  ?= "v5.15/standard/base"
KBRANCH_qemux86-64 ?= "v5.15/standard/base"
KBRANCH_qemumips64 ?= "v5.15/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "2d10943aa7855dc5bbe2c74c6f05ef2e4eb39e3a"
SRCREV_machine_qemuarm64 ?= "b623e8d1eaacbeee250ea37328a9b566ca52f890"
SRCREV_machine_qemumips ?= "7e897b81f922149be5edaab68eb8034a75899ce7"
SRCREV_machine_qemuppc ?= "c3db8eb7b0a5b171ac4c0c6def487b5572a7b3f9"
SRCREV_machine_qemuriscv64 ?= "4eba9348d3e2eeb1070f07bf360a9e404aceb25b"
SRCREV_machine_qemuriscv32 ?= "4eba9348d3e2eeb1070f07bf360a9e404aceb25b"
SRCREV_machine_qemux86 ?= "4eba9348d3e2eeb1070f07bf360a9e404aceb25b"
SRCREV_machine_qemux86-64 ?= "4eba9348d3e2eeb1070f07bf360a9e404aceb25b"
SRCREV_machine_qemumips64 ?= "9868746e0736d8659374e93129cca9d3413a7918"
SRCREV_machine ?= "4eba9348d3e2eeb1070f07bf360a9e404aceb25b"
SRCREV_meta ?= "a43150cc38bed6bf82d9217335348855c1d7eab8"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine_class-devupstream ?= "81d8d30c35edf29c5c70186ccb14dac4a5ca38a8"
PN_class-devupstream = "linux-yocto-upstream"
KBRANCH_class-devupstream = "v5.15/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.15;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.15.35"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES_append_powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES_append_powerpc64 =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP_kernel-vmlinux_qemuppc64 = "textrel"

# devupstream for this case is broken in dunfell and requires later fix
# from commit d0edb03088d0d1c20c899daed1bb3a7110b19670
BBCLASSEXTEND_remove = "devupstream:target"
